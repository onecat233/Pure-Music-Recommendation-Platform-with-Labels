package com.example.music.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.example.music.service.FavourService;
import com.example.music.utils.SessionUtils;
import com.example.music.domain.FavourKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Controller
public class FavourController {
    @Autowired
    private FavourService favourService;

    @ResponseBody
    @RequestMapping(value = "/favour/add", method = RequestMethod.POST)
    public Object addFavour(HttpServletRequest req,HttpSession session){

        JSONObject jsonObject = new JSONObject();
        int id=-1;
        try {
            String encryptedId = (String) session.getAttribute("userId");
            if (encryptedId == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Session expired. Please log in again.");
                return jsonObject;
            }
            String decryptedId = SessionUtils.getDecryptedAttribute(session, "userId");
            if (decryptedId == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Decryption error. Please log in again.");
                return jsonObject;
            }
            id = Integer.parseInt(decryptedId);
        } catch (Exception e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "An error occurred while processing your request. Please log in again.");
            return jsonObject;
        }
        String songid=req.getParameter("songId");
        FavourKey favourKey=new FavourKey();
        favourKey.setUserid(id);
        favourKey.setSongid(Integer.parseInt(songid));
        if (songid == ""){
            jsonObject.put("code", 0);
            jsonObject.put("msg", "no song in your favour");
            return jsonObject;
        }
        FavourKey favour = new FavourKey();
        favour.setUserid(id);
        favour.setSongid(Integer.parseInt(songid));
        boolean res=favourService.addFavour(favour);
        if(res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "Success!");
            return jsonObject;
        }else{
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Fail!");
            return jsonObject;
        }
    }
    @RequestMapping(value = "/favour/detail", method = RequestMethod.GET)
    public Object allFavourofUser(HttpServletRequest req,HttpSession session){
        JSONObject jsonObject = new JSONObject();
        int id=-1;
        try {
            String encryptedId = (String) session.getAttribute("userId");
            if (encryptedId == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Session expired. Please log in again.");
                return jsonObject;
            }
            String decryptedId = SessionUtils.getDecryptedAttribute(session, "userId");
            if (decryptedId == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Decryption error. Please log in again.");
                return jsonObject;
            }
            id = Integer.parseInt(decryptedId);
        } catch (Exception e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "An error occurred while processing your request. Please log in again.");
            return jsonObject;
        }
        return favourService.allFavourofUser(id);
    }

    @RequestMapping(value = "/favour/delete", method = RequestMethod.POST)
    public Object deleteFavour(HttpServletRequest req,HttpSession session){
        JSONObject jsonObject = new JSONObject();
        int id=-1;
        try {
            String encryptedId = (String) session.getAttribute("userId");
            if (encryptedId == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Session expired. Please log in again.");
                return jsonObject;
            }
            String decryptedId = SessionUtils.getDecryptedAttribute(session, "userId");
            if (decryptedId == null) {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Decryption error. Please log in again.");
                return jsonObject;
            }
            id = Integer.parseInt(decryptedId);
        } catch (Exception e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "An error occurred while processing your request. Please log in again.");
            return jsonObject;
        }
        String songid=req.getParameter("songId");
        return favourService.deleteFavour(id,Integer.parseInt(songid));
    } 
}


