package com.vueespring.service.serviceImpl;

import com.vueespring.entity.Thingstable;
import com.vueespring.entity.WebEntity.Item.ItemVOEntity;
import com.vueespring.mapper.ThingstableMapper;
import com.vueespring.service.IThingstableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class ThingstableServiceImpl extends ServiceImpl<ThingstableMapper, Thingstable> implements IThingstableService {
    @Override
    public void setThingByItem(ItemVOEntity item, Thingstable thing) {
        thing.setName(item.getName());
        thing.setStartTime(item.getStartTime());
        thing.setEndTime(item.getEndTime());
        thing.setMessage(item.getMessage());
        thing.setTag(item.getTag());
        thing.setType(item.getType());
        thing.setAlertToken(item.getAlertToken());
    }
}
