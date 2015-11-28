package com.primovision.lutransport.controller.operator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.BaseController;

@Controller
@RequestMapping("/operator")
public class OperatorController extends BaseController {

	@RequestMapping("/home.do")
	public String adminHome(HttpServletRequest request) {
		return "operator/home";
	}
}
