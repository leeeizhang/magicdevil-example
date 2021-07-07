package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.List;

public class Degree implements IEntity {

    private static final long serialVersionUID = 1L;

    private Long did;
    private String dname;

    private List<User> user;

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
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
        result = prime * result + ((did == null) ? 0 : did.hashCode());
        result = prime * result + ((dname == null) ? 0 : dname.hashCode());
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
        Degree other = (Degree) obj;
        if (did == null) {
            if (other.did != null)
                return false;
        } else if (!did.equals(other.did))
            return false;
        if (dname == null) {
            if (other.dname != null)
                return false;
        } else if (!dname.equals(other.dname))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Degree [did=" + did + ", dname=" + dname + "]";
    }

}
