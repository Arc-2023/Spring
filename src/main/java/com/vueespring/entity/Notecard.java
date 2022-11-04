package com.vueespring.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author Cyk
 * @since 2022-10-27
 */
@ApiModel(value = "Notecard对象", description = "")
public class Notecard implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userid;
    private String title;
    private String data;
    private String creater;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    @Override
    public String toString() {
        return "Notecard{" +
            "id = " + id +
            ", userid = " + userid +
            ", title = " + title +
            ", data = " + data +
            ", creater = " + creater +
        "}";
    }
}
