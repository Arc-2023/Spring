package com.vueespring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueespring.entity.Note;
import com.vueespring.entity.Notecard;
import com.vueespring.service.INoteService;
import com.vueespring.service.INotecardService;
import com.vueespring.utils.JsonResult;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpRequest;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.swing.text.html.parser.Entity;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class NoteController {

    String path = "D:\\image\\spb";

    @Autowired
    public INotecardService iNotecardService;
    @Autowired
    public INoteService iNoteService;

    @GetMapping("/getAllnotes")
    @RequiresAuthentication
    public JsonResult getAllNotes(String username) {
        QueryWrapper<Notecard> queryWrapper = new QueryWrapper<Notecard>()
                .eq("creater", username);
        List<Notecard> list = iNotecardService.list(queryWrapper);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tabledata", list);
        return new JsonResult().ok(map);
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
    public JsonResult delNote(String username){
        QueryWrapper<Note> queryWrapper = new QueryWrapper<Note>()
                .eq("creater",username);
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
                .eq("noteid",note.getId());
        if(iNoteService.update(note,queryWrapper)){
            return new JsonResult().ok("submit success");
        }else {
            return new JsonResult().error("submit faild");
        }
    }
}
