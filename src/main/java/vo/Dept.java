package vo;

import java.io.Serializable;

public class Dept implements Serializable {
    private Integer deptno,maxnum,currnum;
    private String dname;

    @Override
    public String toString() {
        return "Dept{" +
                "deptno=" + deptno +
                ", maxnum=" + maxnum +
                ", currnum=" + currnum +
                ", dname='" + dname + '\'' +
                '}';
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public Integer getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(Integer maxnum) {
        this.maxnum = maxnum;
    }

    public Integer getCurrnum() {
        return currnum;
    }

    public void setCurrnum(Integer currnum) {
        this.currnum = currnum;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }
}
