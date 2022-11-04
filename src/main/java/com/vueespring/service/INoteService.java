package com.vueespring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vueespring.entity.Note;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-27
 */
public interface INoteService extends IService<Note> {

    String saveImg(MultipartFile file, String dir) throws IOException;
}
