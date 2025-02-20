package com.example.music.controller;

import com.example.music.domain.Tag;
import javax.servlet.http.HttpServletRequest;
import com.example.music.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@Controller
public class TagController {
    @Autowired
    private TagService tagService;

    @ResponseBody
    @RequestMapping(value="/tag/add",method=RequestMethod.POST)
    public Object addTag(HttpServletRequest req){
        JSONObject jsonObject=new JSONObject();
        String description=req.getParameter("description");
        
        if("".equals(description)){
            jsonObject.put("code",0);
            jsonObject.put("msg","Add error! Tag description should not be empty.");
        }
        Tag tag=new Tag();
        tag.setDescription(description);
        try{
            boolean res=tagService.addTag(tag);
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
            jsonObject.put("code",2);
            jsonObject.put("success", false);
            jsonObject.put("msg","Add fails! Please try again.");
            return jsonObject;
        }
    }

    @RequestMapping(value="/tag/delete",method=RequestMethod.POST)
    public Object deleteTag(HttpServletRequest req){
        String id=req.getParameter("id");
        return tagService.deleteTag(Integer.parseInt(id));
    }
    @RequestMapping(value="/tag/all",method = RequestMethod.GET)
    public Object allTag(){
        return tagService.allTag();
    }
}
