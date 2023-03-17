package com.example.superheltev5.Controller;

import com.example.superheltev5.DTO.HeroFormDTO;
import com.example.superheltev5.Repository.StubRepo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    StubRepo repo = new StubRepo();

    @GetMapping("/register")
    public String showForm(Model model) {

        model.addAttribute("hero", new HeroFormDTO());

        List<String> listCities = Arrays.asList("Gotham", "Rødovre", "Hvidovre", "Nørrebro");
        model.addAttribute("listProfession", listCities);

        return "AddHeroForm";
    }

    @PostMapping("/save")
    public String saveHero(@ModelAttribute HeroFormDTO hero, Model model){
        repo.saveHero(hero);

        model.addAttribute("hero", hero);
        return "ShowRegisteredHero";
    }

}
