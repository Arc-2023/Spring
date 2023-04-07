package com.vueespring.service.serviceImpl;


import com.vueespring.entity.WebEntity.UserEntity;
import com.vueespring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-24
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public UserEntity getuserByUsernamePassword(String username, String password){
        Query QueryUsernamePassword = new Query(Criteria
                .where("username")
                .is(username)
                .and("password")
                .is(password));
        return mongoTemplate.findOne(QueryUsernamePassword,UserEntity.class);
    }
    @Override
    public UserEntity getUserByUsername(String username){
        Query QueryUsername = new Query(
                Criteria.where("username")
                        .is(username)
        );
        return mongoTemplate.findOne(QueryUsername,UserEntity.class);
    }
    @Override
    public UserEntity getUserById(String id){
        Query QueryUsername = new Query(
                Criteria.where("id")
                        .is(id)
        );
        return mongoTemplate.findOne(QueryUsername,UserEntity.class);
    }



}
