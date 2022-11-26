package com.vueespring.service.serviceImpl;

import com.vueespring.service.IOService;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;

@Data
@Component
@Service
public class IOServiceImpl implements IOService {
    @Override
    public Boolean delImgByPath(String path){
       File filepath = new File(path);
       if(filepath.exists()){
           filepath.delete();
           return true;
       }else {
           return false;
       }
    }

}
