package com.softeng.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Test Controller")
public class TestController {

    @GetMapping("/test")
    @Operation(summary = "Test Endpoint")
    public String testEndpoint() {
        return "API is Working!";
    }
}