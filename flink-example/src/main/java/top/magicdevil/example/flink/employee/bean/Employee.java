package top.magicdevil.example.flink.employee.bean;

import java.util.Objects;

public class Employee {
    private Integer eid;
    private String name;
    private Short sex;
    private Long birthday;

    public Employee() {
    }

    public Employee(Integer eid, String name, Short sex, Long birthday) {
        this.eid = eid;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(eid, employee.eid) && Objects.equals(name, employee.name) && Objects.equals(sex, employee.sex) && Objects.equals(birthday, employee.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, name, sex, birthday);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "eid=" + eid +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                '}';
    }
}
