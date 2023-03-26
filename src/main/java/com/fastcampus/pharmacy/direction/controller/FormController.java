package com.fastcampus.pharmacy.direction.controller;

import com.fastcampus.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class FormController {

    private final DirectionService directionService;

    @GetMapping
    public String main() {
        return "main";
    }
}
