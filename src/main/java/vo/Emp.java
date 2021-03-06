package vo;

import java.io.Serializable;
import java.util.Date;

public class Emp implements Serializable {
    private String mid, ename, job, photo;
    private Integer empno;
    private Integer deptno;
    private Integer lid;
    private Integer flag;
    private Double sal, comm;
    private Date hiredate;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Double getSal() {
        return sal;
    }


    public void setSal(Double sal) {
        this.sal = sal;
    }

    public Double getComm() {
        return comm;
    }

    public void setComm(Double comm) {
        this.comm = comm;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "mid='" + mid + '\'' +
                ", ename='" + ename + '\'' +
                ", job='" + job + '\'' +
                ", photo='" + photo + '\'' +
                ", empno=" + empno +
                ", deptno=" + deptno +
                ", lid=" + lid +
                ", flag=" + flag +
                ", sal=" + sal +
                ", comm=" + comm +
                ", hiredate=" + hiredate +
                '}';
    }
}
