package com.primovision.lutransport.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.BaseController;

@Controller
@RequestMapping("/admin")
public class MainController extends BaseController {

	@RequestMapping("/home.do")
	public String adminHome(HttpServletRequest request) {
		return "admin/home";
	}
}
