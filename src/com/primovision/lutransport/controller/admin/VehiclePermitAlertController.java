package com.primovision.lutransport.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.VehiclePermit;


@Controller
@RequestMapping("/admin/vehiclepermitalert")
public class VehiclePermitAlertController extends BaseController{

	@Autowired
	private GenericDAO genericDAO;
	
	public VehiclePermitAlertController(){
		setUrlContext("admin/vehiclepermitalert");
	}
	
	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}
	
	
	@RequestMapping(method = RequestMethod.GET,value="/list.do")
	public String listThirtyDaysData(ModelMap model , HttpServletRequest request) throws ParseException{
		
		
		request.getSession().removeAttribute("searchCriteria");
		Calendar c=new GregorianCalendar();
		Date currentDate = c.getTime();
		c.add(Calendar.DATE, 30);
		Date d=c.getTime();
		//Date d=DateUtil.get30day();
		SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
		//mysqldf.format(d);
		String query="select obj from VehiclePermit obj where obj.status=1 and obj.expirationDate >='"+ mysqldf.format(currentDate) +"' and obj.expirationDate <='"+mysqldf.format(d)+"' order by obj.companyLocation.name asc,obj.vehicle.unit asc,permitNumber asc";
		
		String countquery="select count(obj) from VehiclePermit obj where obj.status=1 and obj.expirationDate >='"+ mysqldf.format(currentDate) +"' and obj.expirationDate <='"+mysqldf.format(d)+"' order by obj.companyLocation.name asc,obj.vehicle.unit asc,permitNumber asc";
		
		List<VehiclePermit> vehiclePermits=genericDAO.executeSimpleQuery(query);
		
		model.addAttribute("list", vehiclePermits);
		
		model.addAttribute("recordCount",genericDAO.executeSimpleQuery(countquery).get(0));
		
		return urlContext+"/alert30";
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET,value="/expiredlist.do")
	public String listExpiredData(ModelMap model , HttpServletRequest request) throws ParseException{
		
		
		request.getSession().removeAttribute("searchCriteria");
		Calendar c=new GregorianCalendar();
		Date currentDate = c.getTime();
		c.add(Calendar.DATE, 30);
		Date d=c.getTime();
		//Date d=DateUtil.get30day();
		SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
		//mysqldf.format(d);
		String query="select obj from VehiclePermit obj where obj.status=1 and obj.expirationDate <'"+ mysqldf.format(currentDate) +"' order by obj.companyLocation.name asc,obj.vehicle.unit asc,permitNumber asc";
		
		String countquery="select count(obj) from VehiclePermit obj where obj.status=1 and obj.expirationDate <'"+ mysqldf.format(currentDate) +"' order by obj.companyLocation.name asc,obj.vehicle.unit asc,permitNumber asc";
		
		List<VehiclePermit> vehiclePermits=genericDAO.executeSimpleQuery(query);
		
		model.addAttribute("list", vehiclePermits);
		
		model.addAttribute("recordCount",genericDAO.executeSimpleQuery(countquery).get(0));
		
		return urlContext+"/alertExpired";
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET,value="/sixtydayslist.do")
	public String listSixtyDaysData(ModelMap model , HttpServletRequest request) throws ParseException{
		
		
		request.getSession().removeAttribute("searchCriteria");
		Calendar c=new GregorianCalendar();
		Date currentDate = c.getTime();
		c.add(Calendar.DATE, 60);
		Date d=c.getTime();
		//Date d=DateUtil.get30day();
		SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
		//mysqldf.format(d);
		String query="select obj from VehiclePermit obj where obj.status=1 and obj.expirationDate >='"+ mysqldf.format(currentDate) +"' and obj.expirationDate <='"+mysqldf.format(d)+"' order by obj.companyLocation.name asc,obj.vehicle.unit asc,permitNumber asc";
		String countquery="select count(obj) from VehiclePermit obj where obj.status=1 and obj.expirationDate >='"+ mysqldf.format(currentDate) +"' and obj.expirationDate <='"+mysqldf.format(d)+"' order by obj.companyLocation.name asc,obj.vehicle.unit asc,permitNumber asc";
		
		List<VehiclePermit> vehiclePermits=genericDAO.executeSimpleQuery(query);
		
		model.addAttribute("list", vehiclePermits);
		
		model.addAttribute("recordCount",genericDAO.executeSimpleQuery(countquery).get(0));
		
		return urlContext+"/alert60";
	}
	
	@RequestMapping(method = RequestMethod.GET,value="/ninetydayslist.do")
	public String listNinetyDaysData(ModelMap model , HttpServletRequest request) throws ParseException{
		
		
		request.getSession().removeAttribute("searchCriteria");
		Calendar c=new GregorianCalendar();
		Date currentDate = c.getTime();
		c.add(Calendar.DATE, 90);
		Date d=c.getTime();
		//Date d=DateUtil.get30day();
		SimpleDateFormat mysqldf = new SimpleDateFormat("yyyy-MM-dd");
		//mysqldf.format(d);
		String query="select obj from VehiclePermit obj where obj.status=1 and obj.expirationDate >='"+ mysqldf.format(currentDate) +"' and obj.expirationDate <='"+mysqldf.format(d)+"' order by obj.companyLocation.name asc,obj.vehicle.unit asc,permitNumber asc";
		String countquery="select count(obj) from VehiclePermit obj where obj.status=1 and obj.expirationDate >='"+ mysqldf.format(currentDate) +"' and obj.expirationDate <='"+mysqldf.format(d)+"' order by obj.companyLocation.name asc,obj.vehicle.unit asc,permitNumber asc";
		List<VehiclePermit> vehiclePermits=genericDAO.executeSimpleQuery(query);
		
		model.addAttribute("list", vehiclePermits);
		
		model.addAttribute("recordCount",genericDAO.executeSimpleQuery(countquery).get(0));
		
		return urlContext+"/alert90";
	}
	
}
