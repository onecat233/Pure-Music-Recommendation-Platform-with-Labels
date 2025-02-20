package com.example.music.service;
import com.example.music.domain.FavourKey;

import java.util.List;
public interface FavourService {
    boolean addFavour(FavourKey favour);
    boolean deleteFavour(Integer userid, Integer songid);
    boolean searchFavour(Integer userid, Integer songid);
    List<FavourKey> allFavourofUser(Integer userid);
    
}
