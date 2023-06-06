package com.vueespring.service.serviceImpl;

import com.vueespring.entity.ThingEnity;
import com.vueespring.entity.WebEntity.Item.Itemtity;
import com.vueespring.service.ThingsService;
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
public class ThingstableServiceImpl implements ThingsService {
    @Override
    public void setThingByItem(Itemtity item, ThingEnity thing) {
        thing.setName(item.getName());
        thing.setStartTime(item.getStartTime());
        thing.setEndTime(item.getEndTime());
        thing.setMessage(item.getMessage());
        thing.setTag(item.getTag());
        thing.setType(item.getType());
        thing.setAlertToken(item.getAlertToken());
    }
}
