package com.example.music.service;

import java.util.List;

import com.example.music.domain.Tag;

public interface TagService {
    boolean addTag(Tag tag);
    boolean deleteTag(Integer tagid);
    List<Tag> allTag();
}