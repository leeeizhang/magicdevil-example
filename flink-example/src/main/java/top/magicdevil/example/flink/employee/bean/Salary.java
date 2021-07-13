package top.magicdevil.example.flink.employee.bean;

import java.util.Objects;

public class Salary {
    private Integer eid;
    private Long payday;
    private Double salary;

    public Salary() {
    }

    public Salary(Integer eid, Long payday, Double salary) {
        this.eid = eid;
        this.payday = payday;
        this.salary = salary;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Long getPayday() {
        return payday;
    }

    public void setPayday(Long payday) {
        this.payday = payday;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salary salary1 = (Salary) o;
        return Objects.equals(eid, salary1.eid) && Objects.equals(payday, salary1.payday) && Objects.equals(salary, salary1.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, payday, salary);
    }

    @Override
    public String toString() {
        return "Salary{" +
                "eid=" + eid +
                ", payday=" + payday +
                ", salary=" + salary +
                '}';
    }
}
