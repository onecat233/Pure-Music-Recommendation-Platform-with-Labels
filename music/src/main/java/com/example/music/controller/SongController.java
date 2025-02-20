package com.example.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.music.domain.Song;
import com.example.music.service.impl.SongServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
public class SongController {
    @Autowired
    private SongServiceImpl songService;

    @RequestMapping(value = "/song/all", method = RequestMethod.GET)
    public Object allSong() {
        return songService.allSong();
    }

    @ResponseBody
    @RequestMapping(value = "/song/add", method = RequestMethod.POST)
    public Object addSong(HttpServletRequest req, @RequestParam("file") MultipartFile mpfile){
        JSONObject jsonObject = new JSONObject();
        String name = req.getParameter("name").trim();
        if (mpfile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Failed to add music. Please try again!");
            return jsonObject;
        }
        String fileName = mpfile.getOriginalFilename();
        String filePath = System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "src"
                + System.getProperty("file.separator")
                + "main"
                + System.getProperty("file.separator")
                + "resources"
                + System.getProperty("file.separator")
                + "static"
                + System.getProperty("file.separator")
                + "songs";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/songs/"+fileName;
        try {
            mpfile.transferTo(dest);
            Song song = new Song();
            song.setName(name);
            song.setUrl(storeUrlPath);
            boolean res = songService.addSong(song);
            if (res) {
                jsonObject.put("code", 1);
                jsonObject.put("avator", storeUrlPath);
                jsonObject.put("msg", "Song added successfully!");
                return jsonObject;
            } else {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Failed to add music. Please try again!");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Failed to add music. Please try again!" + e.getMessage());
            return jsonObject;
        }
    }

    @RequestMapping(value = "/song/detail", method = RequestMethod.POST)
    public Object songOfId(HttpServletRequest req){
        String id = req.getParameter("id");
        return songService.songOfId(Integer.parseInt(id));
    }

    @RequestMapping(value = "/song/search", method = RequestMethod.POST)
    public Object searchSong(HttpServletRequest req){
        String keyword = req.getParameter("keyword");
        return songService.searchSong(keyword);
    }

    @RequestMapping(value = "/song/delete", method = RequestMethod.POST)
    public Object deleteSong(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String id = req.getParameter("id");
        List<Song> oldSongs = songService.songOfId(Integer.parseInt(id));
        if (oldSongs == null || oldSongs.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Song not found!");
            return jsonObject;
        }
            songService.deleteSong(Integer.parseInt(id));
            Song oldSong = oldSongs.get(0);
            if (oldSong.getUrl() != null) {
                String oldFilePath = System.getProperty("user.dir")
                        + System.getProperty("file.separator")
                        + "src"
                        + System.getProperty("file.separator")
                        + "main"
                        + System.getProperty("file.separator")
                        + "resources"
                        + System.getProperty("file.separator")
                        + "static"
                        + oldSong.getUrl().replace("/", System.getProperty("file.separator"));
                File oldFile = new File(oldFilePath);
                if (oldFile.exists() && oldFile.isFile()) {
                    boolean deleted = oldFile.delete();
                    if (!deleted) {
                        jsonObject.put("code", 0);
                        jsonObject.put("warning", "Failed to delete old music file: " + oldFile.getName());
                    }else{
                        jsonObject.put("code", 1);
                        jsonObject.put("msg", "Song deleted successfully!");
                    }
                }
            }
        return jsonObject;
    }


@ResponseBody
@RequestMapping(value = "/song/update", method = RequestMethod.POST)
public Object updateSong(HttpServletRequest req,@RequestParam("file") MultipartFile urlfile){
        JSONObject jsonObject = new JSONObject();
        if (urlfile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Failed to update music. Please try again!");
            return jsonObject;
        }
        String id = req.getParameter("id").trim();
        List<Song> oldSongs = songService.songOfId(Integer.parseInt(id));
        if (oldSongs == null || oldSongs.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Song not found!");
            return jsonObject;
        }
        if (urlfile.isEmpty()) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Failed to update music. Please try again!");
            return jsonObject;
        }
        String name = req.getParameter("name").trim();
        Song song = new Song();
        song.setId(Integer.parseInt(id));
        song.setName(name);
        String fileName = urlfile.getOriginalFilename();
    String filePath = System.getProperty("user.dir")
            + System.getProperty("file.separator")
            + "src"
            + System.getProperty("file.separator")
            + "main"
            + System.getProperty("file.separator")
            + "resources"
            + System.getProperty("file.separator")
            + "static"
            + System.getProperty("file.separator")
            + "songs";
    File file1 = new File(filePath);

    if (!file1.exists()) {
            file1.mkdir();
        }
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/songs/"+fileName;
        try {
            urlfile.transferTo(dest);
            song.setUrl(storeUrlPath);
            boolean res = songService.updateSong(song);
            if (res) {
                Song oldSong = oldSongs.get(0);
                if (oldSong.getUrl() != null) {
                    String oldFilePath = System.getProperty("user.dir") + oldSong.getUrl().replace("/", System.getProperty("file.separator"));
                    File oldFile = new File(oldFilePath);
                    if (oldFile.exists() && oldFile.isFile()) {
                        boolean deleted = oldFile.delete();
                        if (!deleted) {
                            jsonObject.put("code", 0);
                            jsonObject.put("warning", "Failed to delete old music file: " + oldFile.getName());
                        }else{
                            jsonObject.put("code", 1);
                            jsonObject.put("avator", storeUrlPath);
                            jsonObject.put("msg", "Song updated successfully!");
                        }
                    }
                }
                return jsonObject;
            } else {
                jsonObject.put("code", 0);
                jsonObject.put("msg", "Failed to update music. Please try again!");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "Failed to update music. Please try again!" + e.getMessage());
            return jsonObject;
        }
    }
}

