package com.example.controller;

import com.example.dto.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/attr")
public class ProtectedController {
    
    /**
     * 有对应授权规则
     * @return 商品属性
     */
    @GetMapping("/info0")
    public ResponseVO getAttr() {
        return ResponseVO.success().setData("some attr 0");
    }
    
    /**
     * 无对应授权规则
     * @return 商品属性
     */
    @GetMapping("/info1")
    public ResponseVO getAttr1() {
        return ResponseVO.success().setData("some attr 1");
    }
}
