package com.vueespring.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vueespring.entity.WebEntity.UserVoeEntity;
import com.vueespring.mapper.UserVoeTableMapper;
import com.vueespring.service.IUserVoeTableService;
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
public class UserVoeTableServiceImpl extends ServiceImpl<UserVoeTableMapper,UserVoeEntity> implements IUserVoeTableService{

}
