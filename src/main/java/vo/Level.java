package vo;

import java.io.Serializable;

public class Level implements Serializable {
    private String title,flag;
    private Integer lid;
    private Double losal,hisal;

    @Override
    public String toString() {
        return "Level{" +
                "title='" + title + '\'' +
                ", flag='" + flag + '\'' +
                ", lid=" + lid +
                ", losal=" + losal +
                ", hisal=" + hisal +
                ", role=" + role +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public Double getLosal() {
        return losal;
    }

    public void setLosal(Double losal) {
        this.losal = losal;
    }

    public Double getHisal() {
        return hisal;
    }

    public void setHisal(Double hisal) {
        this.hisal = hisal;
    }

    private Role role=new Role();

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
