package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.List;

public class Publisher implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long pid;
    private String pname;

    private List<Book> book;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
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
        result = prime * result + ((pid == null) ? 0 : pid.hashCode());
        result = prime * result + ((pname == null) ? 0 : pname.hashCode());
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
        Publisher other = (Publisher) obj;
        if (pid == null) {
            if (other.pid != null)
                return false;
        } else if (!pid.equals(other.pid))
            return false;
        if (pname == null) {
            if (other.pname != null)
                return false;
        } else if (!pname.equals(other.pname))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Publisher [pid=" + pid + ", pname=" + pname + "]";
    }

}
