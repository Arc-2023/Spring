package com.vueespring.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Data
@Document("progressentities")
public class ProgressEntity {
    @Id
    String id;
    Integer progress;
    Boolean status;
    String title;
    String intro;
    String creater;

    public void setId(String id) {
        this.id = id;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
}
