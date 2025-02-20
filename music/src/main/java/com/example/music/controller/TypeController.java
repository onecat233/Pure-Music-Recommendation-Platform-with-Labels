package com.example.music.controller;

import javax.servlet.http.HttpServletRequest;
import com.example.music.domain.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.music.service.impl.TypeServiceImpl;

@RestController
@Controller
public class TypeController {
    @Autowired
    private TypeServiceImpl typeService;

    @ResponseBody
    @RequestMapping(value="/type/add",method = RequestMethod.POST)
    public Object addType(HttpServletRequest req) {
        JSONObject jsonObject=new JSONObject();
        String songId=req.getParameter("songId");
        String tagId=req.getParameter("tagId");
        
        Type type=new Type();
        type.setSongid(Integer.parseInt(songId));
        type.setTagid(Integer.parseInt(tagId));
        type.setAgreenum(1);
        
        try{
            boolean res=typeService.addType(type);
            if(res){
                jsonObject.put("code",1);
                jsonObject.put("success", true);
                jsonObject.put("msg", "Add success!");
                jsonObject.put("type","success");
            }
            else{
                jsonObject.put("code",0);
                jsonObject.put("success", false);
                jsonObject.put("msg", "Add fails! Please try again.");
                jsonObject.put("type","error");
            }
            return jsonObject;
        }catch(DuplicateKeyException e){
            jsonObject.put("code", 2);
            jsonObject.put("success", false);
            jsonObject.put("msg", "Add fails! Please try again.");
            jsonObject.put("type", "error");
            return jsonObject;
        }
    }

    @RequestMapping(value="/type/update",method=RequestMethod.POST)
    public Object updateType(HttpServletRequest req){
        JSONObject jsonObject=new JSONObject();
        String songId=req.getParameter("songId");
        String tagId=req.getParameter("tagId");
       // String agreeNum=req.getParameter("agreeNum");
       // Type type=new Type();
        //Integer newagreeNum=Integer.parseInt(agreeNum)+1;
       // type.setSongid(Integer.parseInt(songId));
       // type.setTagid(Integer.parseInt(tagId));
       // type.setAgreenum(newagreeNum);
        boolean res=typeService.updateType(Integer.parseInt(songId),Integer.parseInt(tagId));
        if(res){
            jsonObject.put("code",1);
            jsonObject.put("msg", "Update success!");
        } else{
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Update fails! Please try again.");
        }
        return jsonObject;
    }


    @RequestMapping(value="/type/delete",method=RequestMethod.POST)
    public Object deleteType(HttpServletRequest req){
        String songId=req.getParameter("songId");
        String tagId=req.getParameter("tagId");
        return typeService.deleteType(Integer.parseInt(songId),Integer.parseInt(tagId));
    }

    @RequestMapping(value="/type/all",method = RequestMethod.GET)
    public Object allType(){
        return typeService.allType();
    }

    @RequestMapping(value="/type/songoftag",method = RequestMethod.POST)
    public Object allSongofTag(HttpServletRequest req){
        String tagid=req.getParameter("tagId");
        return typeService.allSongofTag(Integer.parseInt(tagid));
    }
    @RequestMapping(value="/type/song",method = RequestMethod.POST)
    public Object allTypeofSong(HttpServletRequest req){
        String songid=req.getParameter("songId");
        return typeService.allTypeofSong(Integer.parseInt(songid));
    }
}
