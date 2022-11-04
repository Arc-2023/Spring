package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author Cyk
 * @since 2022-10-28
 */
@ApiModel(value = "Note对象", description = "")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer noteid;

    private String content;

    private String creater;

    private LocalDateTime createdTime;

    private LocalDateTime lastedittime;

    private String othermessage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getLastedittime() {
        return lastedittime;
    }

    public void setLastedittime(LocalDateTime lastedittime) {
        this.lastedittime = lastedittime;
    }

    public String getOthermessage() {
        return othermessage;
    }

    public void setOthermessage(String othermessage) {
        this.othermessage = othermessage;
    }

    @Override
    public String toString() {
        return "Note{" +
            "id = " + id +
            ", noteid = " + noteid +
            ", content = " + content +
            ", creater = " + creater +
            ", createdTime = " + createdTime +
            ", lastedittime = " + lastedittime +
            ", othermessage = " + othermessage +
        "}";
    }
}
