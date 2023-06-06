package com.vueespring.service;

import com.vueespring.entity.WebEntity.UserEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-24
 */
public interface UserService{

    UserEntity getuserByUsernamePassword(String username, String password);

    UserEntity getUserByUsername(String username);

    UserEntity getUserById(String id);
}
