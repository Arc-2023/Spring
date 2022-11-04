package com.vueespring.entity.WebEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.ConstructorArgs;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoteCard {
    private String title;
    private String intro;
    private Integer id;
    private String creater;
}
