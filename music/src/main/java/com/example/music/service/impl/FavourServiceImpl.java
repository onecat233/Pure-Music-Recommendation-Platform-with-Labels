package com.example.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.music.dao.FavourMapper;
import com.example.music.domain.FavourKey;
import com.example.music.service.FavourService;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class FavourServiceImpl implements FavourService{
    @Autowired
    private FavourMapper favourMapper;
    @Override
    public boolean addFavour(FavourKey favour){
        return favourMapper.addFavour(favour) > 0;
    }
    @Override
    public boolean deleteFavour(Integer userid,Integer songid){
        return favourMapper.deleteFavour(userid, songid) > 0;
    }
    @Override
    public boolean searchFavour(Integer userid,Integer songid){
        return favourMapper.searchFavour(userid,songid) > 0;
    }
    @Override
    public List<FavourKey> allFavourofUser(Integer userid){
        return favourMapper.allFavourofUser(userid);
    }
}
