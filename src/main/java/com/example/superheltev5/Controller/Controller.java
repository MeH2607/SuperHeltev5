package com.example.superheltev5.Controller;

import com.example.superheltev5.DTO.HeroFormDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/register")
    public String showForm(Model model) {
        HeroFormDTO heroDTO = new HeroFormDTO();
        model.addAttribute("hero", heroDTO);

        List<String> listProfession = Arrays.asList("Developer", "Tester", "Architect");
        model.addAttribute("listProfession", listProfession);

        return "register_form";
    }

}
