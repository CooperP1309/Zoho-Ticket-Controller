package com.anticipate.listr.zoho_client.controllers;

import com.anticipate.listr.zoho_client.services.ZohoClientService;
import com.anticipate.listr.jwt_handling.entities.User;
import com.anticipate.listr.jwt_handling.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/test")
@RestController
public class ZohoClientController {
    private final UserService userService;
    private final ZohoClientService zohoClientService;

    public ZohoClientController(UserService userService) {
        this.userService = userService;
        this.zohoClientService = new ZohoClientService();
    }

    @GetMapping("/me")
    public String getExample() {
        
        return zohoClientService.getExample();
    }

    @GetMapping("/zoho-access-token")
    public String getZohoAccessToken() {
        return zohoClientService.getZohoAccessToken();
    }
}