package com.vueespring.entity;


import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>
 * 
 * </p>
 *
 * @author Cyk
 * @since 2022-10-25
 */
@ApiModel(value = "Thingstable对象", description = "")
@Data
@Document("thingentitys")
public class ThingEnity implements Serializable {

    private static final long serialVersionUID = 1L;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;

    private String userid;

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String name;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    private Integer type;

    private String message;

    private String tag;

    private String alertToken;

    private String creater;

    private String status;


    public String getUserid() {
        return userid;
    }

}
