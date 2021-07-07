package top.magicdevil.example.webapp.sample.bookstore.entity;

public class OrderItem implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long oiid;
    private UserOrder userorder;
    private Book book;
    private Long num;

    public Long getOiid() {
        return oiid;
    }

    public void setOiid(Long oiid) {
        this.oiid = oiid;
    }

    public UserOrder getUserorder() {
        return userorder;
    }

    public void setUserorder(UserOrder userorder) {
        this.userorder = userorder;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((book == null) ? 0 : book.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((oiid == null) ? 0 : oiid.hashCode());
        result = prime * result + ((userorder == null) ? 0 : userorder.hashCode());
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
        OrderItem other = (OrderItem) obj;
        if (book == null) {
            if (other.book != null)
                return false;
        } else if (!book.equals(other.book))
            return false;
        if (num == null) {
            if (other.num != null)
                return false;
        } else if (!num.equals(other.num))
            return false;
        if (oiid == null) {
            if (other.oiid != null)
                return false;
        } else if (!oiid.equals(other.oiid))
            return false;
        if (userorder == null) {
            if (other.userorder != null)
                return false;
        } else if (!userorder.equals(other.userorder))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrderItem [oiid=" + oiid + ", userorder=" + userorder + ", book=" + book + ", num="
                + num + "]";
    }

}
