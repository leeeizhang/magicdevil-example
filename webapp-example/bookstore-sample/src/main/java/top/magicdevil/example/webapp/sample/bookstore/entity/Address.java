package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.List;

public class Address implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long aid;
    private User user;
    private String location;

    private List<UserOrder> userorder;

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<UserOrder> getUserorder() {
        return userorder;
    }

    public void setUserorder(List<UserOrder> userorder) {
        this.userorder = userorder;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aid == null) ? 0 : aid.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
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
        Address other = (Address) obj;
        if (aid == null) {
            if (other.aid != null)
                return false;
        } else if (!aid.equals(other.aid))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
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
        return "Address [aid=" + aid + ", user=" + user + ", location=" + location + "]";
    }

}
