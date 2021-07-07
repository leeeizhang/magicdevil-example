package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.List;

public class UserOrder implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long uoid;
    private User user;
    private Integer paid;
    private Address address;

    private Double price;
    private Long total;

    private List<OrderItem> orderitem;

    public Long getUoid() {
        return uoid;
    }

    public void setUoid(Long uoid) {
        this.uoid = uoid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderItem> getOrderitem() {
        return orderitem;
    }

    public void setOrderitem(List<OrderItem> orderitem) {
        this.orderitem = orderitem;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((paid == null) ? 0 : paid.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((total == null) ? 0 : total.hashCode());
        result = prime * result + ((uoid == null) ? 0 : uoid.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        UserOrder other = (UserOrder) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (paid == null) {
            if (other.paid != null)
                return false;
        } else if (!paid.equals(other.paid))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (total == null) {
            if (other.total != null)
                return false;
        } else if (!total.equals(other.total))
            return false;
        if (uoid == null) {
            if (other.uoid != null)
                return false;
        } else if (!uoid.equals(other.uoid))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserOrder [uoid=" + uoid + ", user=" + user + ", paid=" + paid + ", address="
                + address + ", price=" + price + ", total=" + total + "]";
    }

}
