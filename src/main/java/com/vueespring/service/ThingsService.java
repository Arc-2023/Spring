package com.vueespring.service;

import com.vueespring.entity.ThingEnity;
import com.vueespring.entity.WebEntity.Item.Itemtity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-24
 */
public interface ThingsService {

    void setThingByItem(Itemtity item, ThingEnity thing);
}
