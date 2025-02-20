package com.example.music.service;

import com.example.music.domain.Song;

import java.util.List;

public interface SongService{
    boolean addSong (Song song);
    boolean updateSong (Song song);
    boolean deleteSong(Integer id);

    List<Song> searchSong(String keyword);;
    List<Song> songOfId(Integer id);
    List<Song> allSong();
}