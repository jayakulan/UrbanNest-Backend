package com.jayakulan.urbannest.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class TestController {

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails) {
        return "Protected profile data for: " + userDetails.getUsername();
    }
}
