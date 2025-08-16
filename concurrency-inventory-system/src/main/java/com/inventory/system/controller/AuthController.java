package com.inventory.system.controller;


import com.inventory.system.model.AuthConfig;
import com.inventory.system.util.JWTUtil;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtil jwtUtil;


    public AuthController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthConfig authConfig){
        if("admin".equals(authConfig.getUsername())&& "admin".equals(authConfig.getPassword())){
            return ResponseEntity.ok(jwtUtil.generateToken(authConfig.getUsername()));
        }
        return ResponseEntity.status(401).body("Invalid Credentials");
    }
}
