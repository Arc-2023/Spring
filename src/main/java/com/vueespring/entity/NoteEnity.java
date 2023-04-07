package com.vueespring.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>
 * 
 * </p>
 *
 * @author Cyk
 * @since 2022-11-04
 */

@ApiModel(value = "Note对象", description = "")
@Document("noteenitys")
public class NoteEnity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String title;

    private String creater;

    private String content;

    private String othermessage;

    private LocalDateTime createdTime;

    private LocalDateTime lastedittime;

    private String intro;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOthermessage() {
        return othermessage;
    }

    public void setOthermessage(String othermessage) {
        this.othermessage = othermessage;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "Note{" +
            "id = " + id +
            ", title = " + title +
            ", creater = " + creater +
            ", content = " + content +
            ", othermessage = " + othermessage +
            ", createdTime = " + createdTime +
            ", lastedittime = " + lastedittime +
            ", intro = " + intro +
        "}";
    }
}
