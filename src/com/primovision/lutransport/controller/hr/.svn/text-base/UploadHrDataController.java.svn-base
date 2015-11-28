package com.primovision.lutransport.controller.hr;


import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.configuration.Configuration;

import com.primovision.lutransport.service.HrImportMainSheetService;
/**
 * @author subodh
 *
 */

@Controller
@RequestMapping("/hr/uploadData")
@SuppressWarnings("unchecked")
public class UploadHrDataController extends BaseController {
	
	@Autowired
	private HrImportMainSheetService hrimportMainSheetService;

	
	public void setHrimportMainSheetService(
			HrImportMainSheetService hrimportMainSheetService) {
		this.hrimportMainSheetService = hrimportMainSheetService;
	}
	
	@RequestMapping("/attendance.do")
	public String attendance(HttpServletRequest request,
			HttpServletResponse response) {
		return "hr/uploadData/attendance";
	}
	
	

	

	@RequestMapping("/attendance/upload.do")
	public String fuellogSaveData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {
		
		List<String> str=new ArrayList<String>();
		//boolean flag=false;
		try 
		{
			if (StringUtils.isEmpty(file.getOriginalFilename())) 
			{
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "hr/uploadData/attendance";
		    }
			if (!StringUtils.isEmpty(file.getOriginalFilename())) 
			{
				String ext=	file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!(ext.equalsIgnoreCase(".xls")))
                {
                	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
                	return "hr/uploadData/attendance";
				}
			}
			
			InputStream is = file.getInputStream();
			str=hrimportMainSheetService.importAttendanceMainSheet(is);
			System.out.println("\nimportMainSheetService.importMainSheet(is)\n");
			if(str.isEmpty())
			{
				model.addAttribute("msg","successfully uploaded attendance");
			}
			else
			{
				
				model.addAttribute("errorList", str);
			}
		} 
		catch (Exception ex) 
		{
			model.addAttribute("errors", "An error occurred while upload !!");
			ex.printStackTrace();
			log.warn("Unable to import :===>>>>>>>>>" + ex);
		}
		return "hr/uploadData/attendance";
	}
	
}