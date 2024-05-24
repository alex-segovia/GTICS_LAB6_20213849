package com.example.laboratorio6.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservaController {
    @GetMapping(value = "/reservas")
    public String reservas(Model model){

        return "reservas";
    }
}
