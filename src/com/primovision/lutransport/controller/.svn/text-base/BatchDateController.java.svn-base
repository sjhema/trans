package com.primovision.lutransport.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.model.User;

/**
 * @author ravi
 */
@Controller
@RequestMapping("/billbatchdate")
public class BatchDateController extends BaseController{	
	
	@RequestMapping("/save.do")
	public String save(HttpServletRequest request) {	
		if(request.getParameter("billBatchDate") == ""){
			request.getSession().setAttribute("errors", "Please choose the Batch Date");
			return "billbatch/billbatchdate";
		}else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			String batchdate = request.getParameter("billBatchDate");
			Date billBatchdate = null;
			try{
				billBatchdate = dateFormat.parse(batchdate);
			}catch (Exception e) {
				// TODO: handle exception
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("username", getUser(request).getUsername());
			User user = genericDAO.findByCriteria(User.class, map).get(0);
			user.setBillBatchDate(billBatchdate);		
			genericDAO.save(user);		
			return "redirect:/operator/home.do";
		}
	}
}
