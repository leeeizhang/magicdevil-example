package top.magicdevil.example.flink.nginx.utils;

import java.util.Comparator;
import java.util.Objects;

public class RestrictedMapTest {

    static class TestNode implements Cloneable {
        private String value;
        private int priority;

        public TestNode(String value, int priority) {
            this.value = value;
            this.priority = priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestNode testNode = (TestNode) o;
            return priority == testNode.priority && Objects.equals(value, testNode.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, priority);
        }

        @Override
        protected Object clone() {
            return new TestNode(value, priority);
        }
    }

    public static void main(String[] args) {
        RestrictedMap<String, TestNode> map = new RestrictedMap<>(
                Comparator.comparingInt(node -> node.priority), 3);

        map.put("key_4", new TestNode("value_4", 4));
        map.put("key_6", new TestNode("value_6", 6));
        map.put("key_3", new TestNode("value_3", 3));
        map.put("key_9", new TestNode("value_9", 9));
        map.put("key_2", new TestNode("value_2", 2));
        map.put("key_7", new TestNode("value_7", 7));
        map.put("key_1", new TestNode("value_1", 1));
        map.put("key_5", new TestNode("value_5", 5));
        map.put("key_8", new TestNode("value_8", 8));

        map.forEach((s, testNode) -> System.out.println(s + " | " + testNode.value + " | " + testNode.priority));
    }
}
