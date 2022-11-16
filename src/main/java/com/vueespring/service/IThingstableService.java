package com.vueespring.service;

import com.vueespring.entity.Thingstable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vueespring.entity.WebEntity.Item.ItemVOEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-24
 */
public interface IThingstableService extends IService<Thingstable> {

    void setThingByItem(ItemVOEntity item, Thingstable thing);
}
