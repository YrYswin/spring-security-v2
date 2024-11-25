package com.myspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("This is demo page any roles");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminPage() {
        return ResponseEntity.ok("This is admin panel");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userPage() {
        return ResponseEntity.ok("This is user panel");
    }

    @GetMapping("/user_admin")
    public ResponseEntity<String> userAdminPage() {
        return ResponseEntity.ok("This page for user and admin");
    }

}
