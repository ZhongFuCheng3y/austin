package com.java3y.austin.web.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtConfig {

    private String key = "3a79fb4970284e1b849b3ff26e7e1248";

}

