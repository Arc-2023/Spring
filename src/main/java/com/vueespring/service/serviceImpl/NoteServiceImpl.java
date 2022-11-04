package com.vueespring.service.serviceImpl;

import cn.hutool.core.util.IdUtil;
import com.vueespring.entity.Note;
import com.vueespring.mapper.NoteMapper;
import com.vueespring.service.INoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vueespring.utils.JsonResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-27
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {
    @Override
    public String saveImg(MultipartFile file, String dir) throws IOException {
        String regex = "(?=\\.)(.+)$";
        Matcher matcher = Pattern.compile(regex)
                .matcher(Objects.requireNonNull(file.getOriginalFilename()));
        if(matcher.find()){
            String filepath = dir+"\\"+ IdUtil.fastSimpleUUID()+matcher.group(1);
            File path = new File(filepath);
            if (!path.exists()) {
                if (path.mkdirs()) {
                    file.transferTo(path);
                    return filepath;
                }
            }
        }
        return "Faild";
    }

}
