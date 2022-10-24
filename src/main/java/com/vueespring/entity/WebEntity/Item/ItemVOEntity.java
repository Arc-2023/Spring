package com.vueespring.entity.WebEntity.Item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemVOEntity {
    private Integer id;
    private String name;
    private String status;
    private LocalDateTime start;
    private LocalDateTime end;
    private String alertToken;
    private ItemOTHVOEntity othermsg;
}
