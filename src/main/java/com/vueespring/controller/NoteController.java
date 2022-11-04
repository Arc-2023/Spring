package com.vueespring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueespring.entity.Note;
import com.vueespring.entity.WebEntity.NoteCard;
import com.vueespring.mapper.NoteMapper;
import com.vueespring.service.INoteService;
import com.vueespring.utils.JsonResult;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
public class NoteController {

    String path = "D:\\image\\spb";
    @Autowired
    public INoteService iNoteService;
    @Autowired
    NoteMapper noteMapper;

    @GetMapping("/getAllnotes")
    @RequiresAuthentication
    public JsonResult getAllNotes(String username) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<Note>()
                .eq("creater", username);
        List<NoteCard> cards = iNoteService.getNoteCards(queryWrapper);
        return new JsonResult().ok(cards);
    }

    @GetMapping("/getnotecontent")
    @RequiresAuthentication
    public JsonResult getNoteContent(Integer id) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<Note>()
                .eq("id", id);
        Note note = iNoteService.getOne(queryWrapper);
        if (note != null) {
            return new JsonResult().ok(note);
        }
        return new JsonResult().error("Not Found");
    }
    @PostMapping("/uploadimage")
    public JsonResult uploadImage(
            @RequestParam("username") String username,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        String result = iNoteService.saveImg(file,path+"\\"+username);
        if(!result.equals("Faild")){
            return new JsonResult().ok("smb:\\192.168.2.247\\" + result);
        }
        return new JsonResult().error("faild");
    }
    @GetMapping("/delnote")
    public JsonResult delNote(Integer id){
        QueryWrapper<Note> queryWrapper = new QueryWrapper<Note>()
                .eq("id",id);
        if(iNoteService.remove(queryWrapper)){
            return new JsonResult().ok("del Success");
        }else {
            return new JsonResult().error("del faild");
        }
    }
    @PostMapping ("/submitcontent")
    public JsonResult submitNote(
            @RequestBody Note note){
        QueryWrapper<Note> queryWrapper = new QueryWrapper<Note>()
                .eq("id",note.getId());
        if(iNoteService.update(note,queryWrapper)){
            return new JsonResult().ok("submit success");
        }else {
            return new JsonResult().error("submit faild");
        }
    }
    @PostMapping("/addnote")
    public JsonResult addnote(@RequestBody(required = false) NoteCard card) {
                             // @RequestParam("title") String title,
                             // @RequestParam("data") String data){
        Note note = new Note();
        note.setCreater(card.getCreater());
        note.setIntro(card.getIntro());
        note.setTitle(card.getTitle());
        QueryWrapper<Note> queryWrapper = new QueryWrapper<Note>()
                .eq("creater",card.getCreater())
                .eq("title",card.getTitle());
        if(iNoteService.count(queryWrapper)>0) return new JsonResult().error("existing title");
        if(noteMapper.insert(note)!=1) return new JsonResult().error("faild to insert");
        else return new JsonResult().ok(iNoteService.getOne(queryWrapper).getId());
    }
}
