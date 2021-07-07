package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.List;

public class Book implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long bid;
    private String bname;
    private String isbn;
    private Double price;
    private Integer stock;
    private String pic;
    private BookType booktype;
    private Publisher publisher;

    private List<UserBookVisit> userbookvisit;
    private List<OrderItem> orderitem;

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public BookType getBooktype() {
        return booktype;
    }

    public void setBooktype(BookType booktype) {
        this.booktype = booktype;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<UserBookVisit> getUserbookvisit() {
        return userbookvisit;
    }

    public void setUserbookvisit(List<UserBookVisit> userbookvisit) {
        this.userbookvisit = userbookvisit;
    }

    public List<OrderItem> getOrderitem() {
        return orderitem;
    }

    public void setOrderitem(List<OrderItem> orderitem) {
        this.orderitem = orderitem;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bid == null) ? 0 : bid.hashCode());
        result = prime * result + ((bname == null) ? 0 : bname.hashCode());
        result = prime * result + ((booktype == null) ? 0 : booktype.hashCode());
        result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
        result = prime * result + ((pic == null) ? 0 : pic.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
        result = prime * result + ((stock == null) ? 0 : stock.hashCode());
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
        Book other = (Book) obj;
        if (bid == null) {
            if (other.bid != null)
                return false;
        } else if (!bid.equals(other.bid))
            return false;
        if (bname == null) {
            if (other.bname != null)
                return false;
        } else if (!bname.equals(other.bname))
            return false;
        if (booktype == null) {
            if (other.booktype != null)
                return false;
        } else if (!booktype.equals(other.booktype))
            return false;
        if (isbn == null) {
            if (other.isbn != null)
                return false;
        } else if (!isbn.equals(other.isbn))
            return false;
        if (pic == null) {
            if (other.pic != null)
                return false;
        } else if (!pic.equals(other.pic))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (publisher == null) {
            if (other.publisher != null)
                return false;
        } else if (!publisher.equals(other.publisher))
            return false;
        if (stock == null) {
            if (other.stock != null)
                return false;
        } else if (!stock.equals(other.stock))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Book [bid=" + bid + ", bname=" + bname + ", isbn=" + isbn + ", price=" + price
                + ", stock=" + stock + ", pic=" + pic + ", booktype=" + booktype + ", publisher="
                + publisher + "]";
    }

}
