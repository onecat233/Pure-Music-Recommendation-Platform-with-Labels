package com.example.music.service;

import java.util.List;

import com.example.music.domain.Type;

public interface TypeService {
    boolean addType(Type type);
    boolean deleteType(Integer songid, Integer tagid);
    boolean updateType(Integer songid, Integer tagid);
    List<Type> allType();
    List<Type> allTypeofSong(Integer songid);
    List<Type> allSongofTag(Integer tagid);
}
