package vo;

import java.io.Serializable;

public class Action implements Serializable {
    private String title,flag;
    private Integer actid;

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

    public Integer getActid() {
        return actid;
    }

    public void setActid(Integer actid) {
        this.actid = actid;
    }

    @Override
    public String toString() {
        return "Action{" +
                "title='" + title + '\'' +
                ", flag='" + flag + '\'' +
                ", actid=" + actid +
                '}';
    }
}
