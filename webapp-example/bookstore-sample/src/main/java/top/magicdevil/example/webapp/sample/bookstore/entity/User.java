package top.magicdevil.example.webapp.sample.bookstore.entity;

import java.util.Date;
import java.util.List;

public class User implements IEntity {

    private static final long serialVersionUID = 1L;

    public static final int BOY = 0;
    public static final int GIRL = 1;

    private Long uid;
    private String uname;
    private String password;
    private Integer sex;
    private Date birth;
    private String phone;
    private String email;
    private Career career;
    private Degree degree;
    private Integer admin_tag;

    private List<Address> address;
    private List<UserOrder> userorder;
    private List<UserBookVisit> userbookvisit;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Integer getAdmin_tag() {
        return admin_tag;
    }

    public void setAdmin_tag(Integer admin_tag) {
        this.admin_tag = admin_tag;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<UserOrder> getUserorder() {
        return userorder;
    }

    public void setUserorder(List<UserOrder> userorder) {
        this.userorder = userorder;
    }

    public List<UserBookVisit> getUserbookvisit() {
        return userbookvisit;
    }

    public void setUserbookvisit(List<UserBookVisit> userbookvisit) {
        this.userbookvisit = userbookvisit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((admin_tag == null) ? 0 : admin_tag.hashCode());
        result = prime * result + ((birth == null) ? 0 : birth.hashCode());
        result = prime * result + ((career == null) ? 0 : career.hashCode());
        result = prime * result + ((degree == null) ? 0 : degree.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
        result = prime * result + ((uname == null) ? 0 : uname.hashCode());
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
        User other = (User) obj;
        if (admin_tag == null) {
            if (other.admin_tag != null)
                return false;
        } else if (!admin_tag.equals(other.admin_tag))
            return false;
        if (birth == null) {
            if (other.birth != null)
                return false;
        } else if (!birth.equals(other.birth))
            return false;
        if (career == null) {
            if (other.career != null)
                return false;
        } else if (!career.equals(other.career))
            return false;
        if (degree == null) {
            if (other.degree != null)
                return false;
        } else if (!degree.equals(other.degree))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (phone == null) {
            if (other.phone != null)
                return false;
        } else if (!phone.equals(other.phone))
            return false;
        if (sex == null) {
            if (other.sex != null)
                return false;
        } else if (!sex.equals(other.sex))
            return false;
        if (uid == null) {
            if (other.uid != null)
                return false;
        } else if (!uid.equals(other.uid))
            return false;
        if (uname == null) {
            if (other.uname != null)
                return false;
        } else if (!uname.equals(other.uname))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [uid=" + uid + ", uname=" + uname + ", password=" + password + ", sex=" + sex
                + ", birth=" + birth + ", phone=" + phone + ", email=" + email + ", career="
                + career + ", degree=" + degree + ", admin_tag=" + admin_tag + "]";
    }

}
