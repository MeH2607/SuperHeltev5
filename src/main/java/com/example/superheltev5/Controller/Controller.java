package com.example.superheltev5.Controller;

import com.example.superheltev5.DTO.HeroFormDTO;
import com.example.superheltev5.Repository.DBRepo;
import com.example.superheltev5.Repository.StubRepo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    StubRepo repo = new StubRepo();
    DBRepo dbRepo = new DBRepo();
    
    @GetMapping("/table")
    public String showAllHeroes(Model model){
        List<HeroFormDTO>heroList = dbRepo.getAllHeroesDB();
        model.addAttribute("heroList", heroList);
        return "ShowAllHeroes";
    }

   @GetMapping("/table/powers/{heroName}")
   public String showAllPowersForHero(@PathVariable String heroName, Model model){
        HeroFormDTO hero = dbRepo.getHeroSearch(heroName);

        model.addAttribute("hero", hero);
        return "showPowers";
   }

    @GetMapping("/register")
    public String showForm(Model model) {

        model.addAttribute("hero", new HeroFormDTO());
        List<String> listCities = new ArrayList<>(List.of("Gotham", "Rødovre", "Hvidovre", "Nørrebro"));
        model.addAttribute("listCities", listCities);
        List<String> listPowers = new ArrayList<>(List.of("Flying", "Super Strength", "Lasers", "Money"));
        model.addAttribute("listPower", listPowers);
        return "AddHeroForm";
    }

    @PostMapping("/register")
    public String saveHero(@ModelAttribute("hero") HeroFormDTO hero){


        dbRepo.createHero(hero);
    //    model.addAttribute("hero", hero);
        return "registerSucces";
    }

    @GetMapping("/delete")
    public String confirmDeleteHero(@RequestParam String heroName, Model model, HeroFormDTO hero){
        hero = dbRepo.getHeroSearch(heroName);
        model.addAttribute("hero", hero);
        return "DeleteConfirmation";
    }


        @PostMapping("/delete")
    public String deleteHero(@RequestParam String heroName){

        HeroFormDTO hero = dbRepo.getHeroSearch(heroName);

        dbRepo.deleteHero(hero);

        return "deleteSuccess";
    }

}
