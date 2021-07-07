package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.Date;

public class UserBookVisit implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long vid;
    private User user;
    private Book book;
    private Date vtime;

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getVtime() {
        return vtime;
    }

    public void setVtime(Date vtime) {
        this.vtime = vtime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((book == null) ? 0 : book.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((vid == null) ? 0 : vid.hashCode());
        result = prime * result + ((vtime == null) ? 0 : vtime.hashCode());
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
        UserBookVisit other = (UserBookVisit) obj;
        if (book == null) {
            if (other.book != null)
                return false;
        } else if (!book.equals(other.book))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (vid == null) {
            if (other.vid != null)
                return false;
        } else if (!vid.equals(other.vid))
            return false;
        if (vtime == null) {
            if (other.vtime != null)
                return false;
        } else if (!vtime.equals(other.vtime))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserBookVisit [vid=" + vid + ", user=" + user + ", book=" + book + ", vtime="
                + vtime + "]";
    }

}
