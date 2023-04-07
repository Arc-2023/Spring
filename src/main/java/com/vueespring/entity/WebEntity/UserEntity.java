package com.vueespring.entity.WebEntity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Cyk
 * @since 2022-12-04
 */
@AllArgsConstructor
@NoArgsConstructor
@Document("users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private String username;

    private String password;

    private String permission;

    private String alertToken;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAlertToken() {
        return alertToken;
    }

    public void setAlertToken(String alertToken) {
        this.alertToken = alertToken;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
            "id = " + id +
            ", username = " + username +
            ", password = " + password +
            ", permission = " + permission +
            ", alertToken = " + alertToken +
        "}";
    }
}
