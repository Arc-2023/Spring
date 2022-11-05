package com.vueespring.service.serviceImpl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueespring.entity.Note;
import com.vueespring.entity.WebEntity.NoteCard;
import com.vueespring.mapper.NoteMapper;
import com.vueespring.service.INoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        //匹配扩展名
        String regex = "(?=\\.)(.+)$";
        Matcher matcher = Pattern.compile(regex)
                .matcher(Objects.requireNonNull(file.getOriginalFilename()));
        if(matcher.find()){
            String filename = IdUtil.fastSimpleUUID()+matcher.group(1);
            String filepath = dir+"\\"+ IdUtil.fastSimpleUUID()+matcher.group(1);
            File path = new File(filepath);
            if (!path.exists()) {
                if (path.mkdirs()) {
                    file.transferTo(path);
                    return "http://192.168.2.247/" + filename;
                }
            }
        }
        return null;
    }
    @Override
    public List<NoteCard> getNoteCards(QueryWrapper<Note> wrapper){
        NoteCard card = new NoteCard();
        List<NoteCard> cards = new ArrayList<NoteCard>();
        list(wrapper).parallelStream().forEach(item->{
            card.setTitle(item.getTitle());
            card.setId(item.getId());
            card.setCreater(item.getCreater());
            card.setIntro(item.getIntro());
            cards.add(card);
        });
        return cards;
    }

}
