package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.List;

public class BookType implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long tid;
    private String tname;

    private List<Book> book;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tid == null) ? 0 : tid.hashCode());
        result = prime * result + ((tname == null) ? 0 : tname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookType other = (BookType) obj;
        if (tid == null) {
            if (other.tid != null)
                return false;
        } else if (!tid.equals(other.tid))
            return false;
        if (tname == null) {
            if (other.tname != null)
                return false;
        } else if (!tname.equals(other.tname))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BookType [tid=" + tid + ", tname=" + tname + "]";
    }

}
