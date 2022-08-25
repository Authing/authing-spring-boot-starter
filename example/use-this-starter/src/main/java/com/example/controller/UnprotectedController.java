package com.example.controller;

import com.example.dto.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnprotectedController {
    
    @GetMapping("/hello")
    public ResponseVO hello() {
        return ResponseVO.success().setData("hello!!!!");
    }
    
    @PostMapping("/login")
    public ResponseVO login() {
        return ResponseVO.success().setData("login!!!!");
    }
}
