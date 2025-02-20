package com.example.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.music.service.TypeService;
import com.example.music.domain.Type;
import com.example.music.dao.TypeMapper;
import java.util.List;
@Service
public class TypeServiceImpl implements TypeService{
    @Autowired
    private TypeMapper typeMapper;
    @Override
    public boolean addType(Type type){
        return typeMapper.addType(type)>0;
    }
    @Override
    public boolean deleteType(Integer songid, Integer tagid){
        return typeMapper.deleteType(songid,tagid)>0;
    }
    @Override
    public boolean updateType(Integer songid, Integer tagid){
        return typeMapper.updateType(songid, tagid)>0;
    }
    
    @Override
    public List<Type> allType(){
        return typeMapper.allType();
    }
    
    @Override
    public List<Type> allTypeofSong(Integer songid){
        return typeMapper.allTypeofSong(songid);
    }
    @Override
    public List<Type> allSongofTag(Integer tagid){
        return typeMapper.allSongofTag(tagid);
    }
}
