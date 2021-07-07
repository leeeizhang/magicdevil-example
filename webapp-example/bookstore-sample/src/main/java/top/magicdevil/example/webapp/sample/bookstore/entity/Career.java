package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.List;

public class Career implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long cid;
    private String cname;
    
    private List<User> user;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cid == null) ? 0 : cid.hashCode());
        result = prime * result + ((cname == null) ? 0 : cname.hashCode());
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
        Career other = (Career) obj;
        if (cid == null) {
            if (other.cid != null)
                return false;
        } else if (!cid.equals(other.cid))
            return false;
        if (cname == null) {
            if (other.cname != null)
                return false;
        } else if (!cname.equals(other.cname))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Career [cid=" + cid + ", cname=" + cname + "]";
    }

}
