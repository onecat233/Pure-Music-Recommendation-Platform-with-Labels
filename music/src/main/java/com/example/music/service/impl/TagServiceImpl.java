package com.example.music.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.music.dao.TagMapper;
import com.example.music.domain.Tag;
import com.example.music.service.TagService;
import java.util.List;

@Service
public class TagServiceImpl implements TagService{
    @Autowired
    private TagMapper tagMapper;
    @Override
    public boolean addTag(Tag tag){
        return tagMapper.addTag(tag)>0;
    }
    
    @Override
    public boolean deleteTag(Integer tagid){
        return tagMapper.deleteTag(tagid)>0;
    }
    @Override
    public List<Tag> allTag(){
        return tagMapper.allTag();
    }
}
