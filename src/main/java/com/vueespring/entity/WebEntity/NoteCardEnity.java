package com.vueespring.entity.WebEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("notecardenitys")
public class NoteCardEnity implements Serializable {
    @Id
    private String id;
    private String title;
    private String creater;
    private String creator;
    private LocalDateTime editTime;
    private LocalDateTime createdTime;
    private Integer view;
    private String tag;
    private String type;
    private String content;

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getEditTime() {
        return editTime;
    }

    public void setEditTime(LocalDateTime editTime) {
        this.editTime = editTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return "NoteCardEnity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", creater='" + creater + '\'' +
                ", creator='" + creator + '\'' +
                ", editTime=" + editTime +
                ", createdTime=" + createdTime +
                ", view=" + view +
                ", tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }


}
