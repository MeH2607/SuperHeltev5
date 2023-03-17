package com.example.superheltev5.Repository;

import com.example.superheltev5.DTO.HeroFormDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class StubRepo {

    private List<HeroFormDTO> heroList = new ArrayList<>();

    public StubRepo(){
    }

    public void saveHero(HeroFormDTO hero){
        heroList.add(hero);
    }
}
