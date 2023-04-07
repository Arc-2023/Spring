package com.vueespring.service;

import com.vueespring.entity.ThingEnity;
import com.vueespring.entity.WebEntity.Item.Itemtity;
import org.springframework.data.mongodb.core.query.Update;

public interface UpdateService {
    Update getUpdateByItem(Itemtity item);

    Update updateThingEnity(ThingEnity thingEnity);
}
