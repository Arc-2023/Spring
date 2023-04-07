package com.vueespring.service.serviceImpl;

import com.vueespring.entity.ThingEnity;
import com.vueespring.entity.WebEntity.Item.Itemtity;
import com.vueespring.service.UpdateService;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Override
    public Update getUpdateByItem(Itemtity item) {
        Update update = new Update();
        update.set("name", item.getName());
        update.set("startTime", item.getStartTime());
        update.set("endTime", item.getEndTime());
        update.set("type", item.getType());
        update.set("message", item.getMessage());
        update.set("tag", item.getTag());
        update.set("alertToken", item.getAlertToken());
        return update;
    }

    @Override
    public Update updateThingEnity(ThingEnity thingEnity){
        Update update = new Update();
        update.set("name",thingEnity.getName())
                .set("startTime",thingEnity.getStartTime())
                .set("endTime",thingEnity.getEndTime())
                .set("type",thingEnity.getType())
                .set("message",thingEnity.getMessage())
                .set("tag",thingEnity.getTag())
                .set("status",thingEnity.getStatus());
        return  update;
    }
}
