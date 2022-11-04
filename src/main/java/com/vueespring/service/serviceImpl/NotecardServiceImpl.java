package com.vueespring.service.serviceImpl;

import com.vueespring.entity.Notecard;
import com.vueespring.mapper.NotecardMapper;
import com.vueespring.service.INotecardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-27
 */
@Service
public class NotecardServiceImpl extends ServiceImpl<NotecardMapper, Notecard> implements INotecardService {

}
