package com.example.music.service.impl;

import com.example.music.dao.SongMapper;
import com.example.music.domain.Song;
import com.example.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private SongMapper songMapper;

    @Override
    public List<Song> allSong() {
        return songMapper.allSong();
    }

    @Override
    public List<Song> songOfId(Integer id) {
        return songMapper.songOfId(id);
    }

    @Override
    public List<Song> searchSong(String keyword) {
        return songMapper.searchSong(keyword);
    }

    @Override
    public boolean addSong(Song song) {
        return songMapper.addSong(song) > 0;
    }

    @Override
    public boolean deleteSong(Integer id) {
        return songMapper.deleteSong(id) > 0;
    }

    @Override
    public boolean updateSong(Song song) {
        return songMapper.updateSong(song) > 0;
    }
}
