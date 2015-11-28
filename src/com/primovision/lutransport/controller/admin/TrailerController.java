package com.primovision.lutransport.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.Trailer;

/**
 * @author ravi
 */

@Controller
@RequestMapping("/admin/trailer")
public class TrailerController extends CRUDController<Trailer>{
	
	public TrailerController(){
		urlContext = "admin/trailer";
	}
}
	