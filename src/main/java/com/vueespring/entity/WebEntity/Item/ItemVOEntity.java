package com.vueespring.entity.WebEntity.Item;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemVOEntity {
    private Integer id;
    private Integer userid;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String creater;
    private String tag;
    private Integer type;
    private String message;
    private String alertToken;
}
