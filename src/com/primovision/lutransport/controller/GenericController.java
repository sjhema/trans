package com.primovision.lutransport.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.primovision.lutransport.model.User;

@Controller
public abstract class GenericController {

    protected User getUserInfo(HttpServletRequest request) {
	return (User) request.getSession().getAttribute("userInfo");
    }
}
