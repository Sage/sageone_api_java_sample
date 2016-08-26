package com.sageone.sample.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

// This is just here so that you can check the user information has correctly been retrieved by Spring
@RestController
public class UserController {

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
