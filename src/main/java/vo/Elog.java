package vo;

import java.io.Serializable;

public class Elog implements Serializable {
    private String mid,job,note;
    private Integer elid,empno,deptno,lid,sflag,flag;
    private Double sal,comm;

    @Override
    public String toString() {
        return "Elog{" +
                "mid='" + mid + '\'' +
                ", job='" + job + '\'' +
                ", note='" + note + '\'' +
                ", elid=" + elid +
                ", empno=" + empno +
                ", deptno=" + deptno +
                ", lid=" + lid +
                ", sflag=" + sflag +
                ", flag=" + flag +
                ", sal=" + sal +
                ", comm=" + comm +
                '}';
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getElid() {
        return elid;
    }

    public void setElid(Integer elid) {
        this.elid = elid;
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

    public Integer getSflag() {
        return sflag;
    }

    public void setSflag(Integer sflag) {
        this.sflag = sflag;
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
}
