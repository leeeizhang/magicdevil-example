package top.magicdevil.example.flink.nginx.utils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RestrictedMap<K, V> implements Map<K, V> {

    private final Pair<K, V>[] heapSeq;
    private final Map<K, Integer> heapIndex;
    private final Comparator<V> comparator;
    private final int maxSize;

    private int heapSize;

    public RestrictedMap(Comparator<V> comparator) {
        this.heapSize = 0;
        this.maxSize = 1 << 16;
        this.comparator = comparator;
        this.heapSeq = new ImmutablePair[this.maxSize + 1];
        this.heapIndex = new ConcurrentHashMap<>(this.maxSize);
    }

    public RestrictedMap(Comparator<V> comparator, int maxSize) {
        this.heapSize = 0;
        this.maxSize = maxSize;
        this.comparator = comparator;
        this.heapSeq = new ImmutablePair[this.maxSize + 1];
        this.heapIndex = new ConcurrentHashMap<>(this.maxSize);
    }

    private synchronized void heapAdjust(int pivot) {
        heapSeq[0] = heapSeq[pivot];

        for (int cpivot = pivot << 1; cpivot <= this.heapSize; cpivot <<= 1) {
            if (cpivot + 1 <= this.heapSize &&
                    this.comparator.compare(heapSeq[cpivot].getValue(), heapSeq[cpivot + 1].getValue()) > 0) {
                cpivot++;
            }

            if (this.comparator.compare(heapSeq[0].getValue(), heapSeq[cpivot].getValue()) > 0) {
                heapSeq[pivot] = heapSeq[cpivot];
                heapIndex.replace(heapSeq[cpivot].getKey(), pivot);
                pivot = cpivot;
            } else {
                break;
            }
        }

        heapSeq[pivot] = heapSeq[0];
        heapIndex.replace(heapSeq[pivot].getKey(), pivot);
    }

    private synchronized void heapAdjust() {
        for (int pivot = this.heapSize >> 1; pivot >= 1; pivot--) {
            this.heapAdjust(pivot);
        }
    }

    private synchronized void heapPutAll(Iterator<? extends Entry<? extends K, ? extends V>> iter) {
        boolean updateFlag = false;

        while (iter.hasNext()) {
            Entry<? extends K, ? extends V> entry = iter.next();
            Integer index = this.heapIndex.get(entry.getKey());

            if (index == null) {
                if (this.heapSize != this.maxSize) {
                    this.heapSeq[++this.heapSize] = new ImmutablePair<>(entry.getKey(), entry.getValue());
                    this.heapIndex.put(entry.getKey(), this.heapSize);
                    updateFlag = true;
                } else if (this.comparator.compare(entry.getValue(), heapSeq[1].getValue()) > 0) {
                    this.heapIndex.remove(this.heapSeq[1].getKey());
                    this.heapSeq[1] = new ImmutablePair<>(entry.getKey(), entry.getValue());
                    this.heapIndex.put(entry.getKey(), 1);
                    if (updateFlag) {
                        this.heapAdjust();
                    } else {
                        this.heapAdjust(1);
                    }
                }
            } else if (!this.heapSeq[index].getValue().equals(entry.getValue())) {
                this.heapSeq[index] = new ImmutablePair<>(entry.getKey(), entry.getValue());
                this.heapIndex.put(entry.getKey(), index);
                updateFlag = true;
            }
        }

        if (updateFlag) {
            this.heapAdjust();
        }
    }

    private synchronized V heapPut(K key, V value) {
        this.heapPutAll(
                new HashMap<K, V>() {{
                    this.put(key, value);
                }}.entrySet().iterator()
        );
        return value;
    }

    private synchronized V heapRemove(K key) {
        V heapNode = null;
        if (this.heapIndex.containsKey(key)) {
            int index = this.heapIndex.get(key);
            heapNode = this.heapSeq[index].getValue();

            this.heapSeq[index] = this.heapSeq[this.heapSize--];
            this.heapIndex.remove(key);
            this.heapAdjust(index);
        }
        return heapNode;
    }

    @Override
    public synchronized int size() {
        return this.heapSize;
    }

    @Override
    public synchronized boolean isEmpty() {
        return this.heapSize == 0;
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return this.heapIndex.containsKey(key);
    }

    @Override
    @Deprecated
    public synchronized boolean containsValue(Object value) {
        for (int i = 1; i <= this.heapSize; i++) {
            if (this.heapSeq[i].getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized V get(Object key) {
        return this.heapSeq[this.heapIndex.get(key)].getValue();
    }

    @Override
    public synchronized V put(K key, V value) {
        return this.heapPut(key, value);
    }

    @Override
    public synchronized V remove(Object key) {
        if (this.containsKey(key)) {
            return this.heapRemove((K) key);
        }
        return null;
    }

    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> map) {
        this.heapPutAll(map.entrySet().iterator());
    }

    @Override
    public synchronized void clear() {
        this.heapIndex.clear();
        this.heapSize = 0;
    }

    @Override
    public synchronized Set<K> keySet() {
        return this.heapIndex.keySet();
    }

    @Override
    public synchronized Collection<V> values() {
        List<V> values = new ArrayList<>(this.heapSize);
        for (int i = 1; i <= this.heapSize; i++) {
            values.add(this.heapSeq[i].getValue());
        }
        return values;
    }

    @Override
    public synchronized Set<Entry<K, V>> entrySet() {
        Map<K, V> map = new HashMap<>();
        for (int i = 1; i <= this.heapSize; i++) {
            map.put(this.heapSeq[i].getKey(), this.heapSeq[i].getValue());
        }
        return map.entrySet();
    }

}