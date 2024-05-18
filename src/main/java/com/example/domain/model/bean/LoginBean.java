package com.example.domain.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class LoginBean {

    private boolean result;

    private String jwt;
}
