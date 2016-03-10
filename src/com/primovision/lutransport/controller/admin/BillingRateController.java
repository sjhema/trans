package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.tags.IColumnTag;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Customer;
import com.primovision.lutransport.model.DemurrageCharges;
import com.primovision.lutransport.model.FuelSurchargePadd;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.TonnagePremium;

/**
 * @author ravi
 */

@Controller
@RequestMapping("/admin/billingrate")
public class BillingRateController extends CRUDController<BillingRate>{
	
	public BillingRateController(){
		setUrlContext("/admin/billingrate");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.BaseController#initBinder(org.springframework.web.bind.WebDataBinder)
	 */
	@Override
	@InitBinder
	 public void initBinder(WebDataBinder binder) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(Long.class,false));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(FuelSurchargePadd.class, new AbstractModelEditor(FuelSurchargePadd.class));
		binder.registerCustomEditor(TonnagePremium.class, new AbstractModelEditor(TonnagePremium.class));
		binder.registerCustomEditor(DemurrageCharges.class, new AbstractModelEditor(DemurrageCharges.class));
		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(Customer.class));
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupCreate(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("customernames",
				genericDAO.findByCriteria(Customer.class, criterias, "name", false));
		/*criterias.put("type", 1);
		model.addAttribute("transferStation",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfill",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));*/
		
		
		if(request.getParameter("id")!=null){
			criterias.clear();
			criterias.put("type", 1);
			model.addAttribute("transferStation", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
			criterias.clear();
			criterias.put("type", 2);
			//criterias.put("id!",91l);
			model.addAttribute("landfill", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}else{
			criterias.clear();
			criterias.put("status", 1);
			criterias.put("type", 1);
			model.addAttribute("transferStation", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
			criterias.clear();
			criterias.put("status", 1);
			criterias.put("type", 2);
			//criterias.put("id!",91l);
			model.addAttribute("landfill", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}

		
		
		
		
		
		criterias.put("type", 3);
		model.addAttribute("companyLocation",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
		model.addAttribute("rateTypes", listStaticData("RATE_TYPE"));
		model.addAttribute("billUsing", listStaticData("BILL_USING"));
		model.addAttribute("paddUsing", listStaticData("PADD_USING"));
		model.addAttribute("rateUsing", listStaticData("RATE_USING"));
		model.addAttribute("weeklyRateUsing", listStaticData("WEEKLY_RATE_USING"));
		model.addAttribute("yesNo", listStaticData("YES_NO"));	
		model.addAttribute("fuelSurchargeTypes", listStaticData("FUEL_SURCHARGE_TYPE"));	
		model.addAttribute("padds", genericDAO.findAll(FuelSurchargePadd.class));
		model.addAttribute("tonnages", genericDAO.findAll(TonnagePremium.class));
		model.addAttribute("demurrage", genericDAO.findAll(DemurrageCharges.class));
		/*
		 * Subcontractor Rate Type: Per Load/Per Ton
		 */
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "RATE_TYPE");
		map.put("dataValue", "2,3");
		model.addAttribute("subcontractorRate", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupList(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("transferStation", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		//criterias.put("id!",91l);
		model.addAttribute("landfill", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#save(javax.servlet.http.HttpServletRequest, com.primovision.lutransport.model.BaseModel, org.springframework.validation.BindingResult, org.springframework.ui.ModelMap)
	 */
	
	//from
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {		
		String type=(String) request.getSession().getAttribute("typ");		
		setupUpdate(model, request);		
	     model.addAttribute("type", type);		
		return urlContext + "/form";
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		String type=(String) request.getSession().getAttribute("typ");		
		setupCreate(model, request);			
		 model.addAttribute("type", type);		
		return urlContext + "/form";
	}
	
	
	@Override
	public String delete(@ModelAttribute("modelObject") BillingRate entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
		// return to list
		
		if(request.getSession().getAttribute("typ")!=null)  			
			return "redirect:/rate/expirationreport/list.do?type=billingrate";
		
		return "redirect:/" + urlContext + "/list.do";
	}
	@RequestMapping("/changestatus.do")
	public String changeStatus(HttpServletRequest request, ModelMap modMap) {
		String type=(String) request.getSession().getAttribute("typ");	
		if(type!=null){		
			
			if(request.getParameter("status").equalsIgnoreCase("current")){
				request.getSession().setAttribute("error", "Unable to process your request. Please check rate status");
				return "redirect:/rate/expirationreport/list.do?type=billingrate";				
			}
		BillingRate billingrate = genericDAO.getById(BillingRate.class,
		    Long.valueOf(request.getParameter("id")));
		if (billingrate.getStatus() == 1) 
			billingrate.setStatus(0);		 	
		genericDAO.save(billingrate);
		return "redirect:/rate/expirationreport/list.do?type=billingrate";
		}
		else{			
			BillingRate billingrate = genericDAO.getById(BillingRate.class,
			Long.valueOf(request.getParameter("id")));
			if (billingrate.getStatus() == 0) 
				billingrate.setStatus(1);		 	
			genericDAO.save(billingrate);
			return "redirect:/" + urlContext + "/list.do";
		}
		
	}
	//here
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") BillingRate entity,
			BindingResult bindingResult, ModelMap model) {
		
		   if(entity.getCustomername() == null){
			    bindingResult.rejectValue("customername", "error.select.option", null, null);
		     }
			if(entity.getCompanyLocation() == null){
				bindingResult.rejectValue("companyLocation", "error.select.option", null, null);
			}
			if(entity.getTransferStation() == null){
				bindingResult.rejectValue("transferStation", "error.select.option", null, null);
			}
			if(entity.getLandfill() == null){
				bindingResult.rejectValue("landfill", "error.select.option", null, null);
			}
			if(entity.getRateType() == null){
				bindingResult.rejectValue("rateType", "error.select.option", null, null);
			}
			if(entity.getBillUsing() == null){
				bindingResult.rejectValue("billUsing", "error.select.option", null, null);
			}
			if(entity.getSortBy() == null){
				bindingResult.rejectValue("sortBy", "error.select.option", null, null);
			}
			
           if(entity.getTonnagePremium() == null){
				TonnagePremium tonnege =genericDAO.getById(TonnagePremium.class, 0L);				
				entity.setTonnagePremium(tonnege);
           }
           
			if(!"A".equalsIgnoreCase(entity.getFuelSurchargeType())) {
				FuelSurchargePadd pad =genericDAO.getById(FuelSurchargePadd.class, 1L);
				entity.setPadd(pad);
			}
			if(entity.getRateUsing()== null){
				bindingResult.rejectValue("rateUsing", "error.select.option", null, null);
			}

			
			/*if(entity.getTonnagePremium() == null){
				bindingResult.rejectValue("tonnagePremium", "NotNull.java.lang.Integer", null, null);	
			}*/
			
			/*if(entity.getRateUsing()!= null){
				if (entity.getValidFrom()==null) {
					bindingResult.rejectValue("validFrom", "error.select.option", null, null);
				}
				if (entity.getValidTo()==null) {
					bindingResult.rejectValue("validTo", "error.select.option", null, null);
				}
			}*/
			/*if(entity.getOwnerRate() == 1 && entity.getOwnerRateAmount() == null){ //1 for Yes in static data
				bindingResult.rejectValue("ownerRateAmount", "NotNull.java.lang.Integer", null, null);		
			}*/
			//Fuel SearchCharge
			if(!"N".equalsIgnoreCase(entity.getFuelSurchargeType())) {
				if ("A".equalsIgnoreCase(entity.getFuelSurchargeType()) && entity.getFuelsurchargeweeklyRate()==0) {
					if(entity.getPadd() == null)
						bindingResult.rejectValue("padd", "NotNull.java.lang.Integer", null, null);
					if(entity.getPaddUsing()== null){
						bindingResult.rejectValue("paddUsing", "error.select.option", null, null);
					}
					if(entity.getPeg() == null)
						bindingResult.rejectValue("peg", "NotNull.java.lang.Double", null, null);
					/*if(entity.getMiles() == null)
						bindingResult.rejectValue("miles", "NotNull.java.lang.Double", null, null);*/
				}
				if ("A".equalsIgnoreCase(entity.getFuelSurchargeType()) && entity.getFuelsurchargeweeklyRate()==1) {
					if(entity.getWeeklyRateUsing()== null){
						bindingResult.rejectValue("weeklyRateUsing", "error.select.option", null, null);
					}
				}
				/*
				 * for Manual enter either surcharge per ton or Surcharge amount
				 */
				if("M".equalsIgnoreCase(entity.getFuelSurchargeType())){
					if(entity.getSurchargePerTon() == null && entity.getSurchargeAmount() == null){
						request.getSession().setAttribute("errors", "Please enter either Surcharge Per Ton or Surcharge Amount");
						setupList(model, request);
						model.addAttribute("type",(String) request.getSession().getAttribute("typ"));		
						return urlContext + "/form";
					}
				}
			}
			/*
			 * Subcontractor - If Subcontractor is set to Yes, 
			 * then Subcontractor Rate Type and Subcontractor Rate must be entered.
			 
			if(entity.getSubcontractor() == 1){
				if(entity.getSubcontractorRateType() == null)
					bindingResult.rejectValue("subcontractorRateType", "error.select.option", null, null);
				if(entity.getSubcontractorRate() == null)
					bindingResult.rejectValue("subcontractorRate", "NotNull.java.lang.Double", null, null);
			}*/
			
			// from
			 
			try {
				getValidator().validate(entity, bindingResult);
			} catch (ValidationException e) {
				e.printStackTrace();
				log.warn("Error in validation :" + e);
			}
			// return to form if we had errors
			if (bindingResult.hasErrors()) {
				setupCreate(model, request);
				model.addAttribute("type",(String) request.getSession().getAttribute("typ"));
				return urlContext + "/form";
			}
			
			Date validFromTemp=entity.getValidFromTemp();
			Date validToTemp=entity.getValidToTemp();
			
			if(entity.getValidFromTemp()!=null && entity.getValidToTemp() !=null ){
				
				if(entity.getValidFrom().getTime()== validFromTemp.getTime() && entity.getValidTo().getTime()== validToTemp.getTime()){					
					///// do nothing					
				}
				else{						
					  String rateQuery = "select obj from BillingRate obj where obj.transferStation='"
							+ entity.getTransferStation().getId()
							+ "' and obj.landfill='"					
							+ entity.getLandfill().getId()
							+"' and obj.validFrom !='"
							+ validFromTemp
							+ "' and obj.validTo != '"
							+validToTemp+"'";					  
					  String result=common(request,model,entity,rateQuery);
		               
		               if(result.equalsIgnoreCase("error")){
		            	   System.out.println("here here");
		               setupUpdate(model, request);	
		         		model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
		         		request.getSession().setAttribute("error", "Rate already exists for specified date range.");
		   				return urlContext + "/form"; 
		               }  
			      }		
			}
			else{			
		    String rateQuery = "select obj from BillingRate obj where obj.transferStation='"
				+ entity.getTransferStation().getId()
				+ "' and obj.landfill='"					
				+ entity.getLandfill().getId()+ "' order by obj.validFrom desc";
               String result=common(request,model,entity,rateQuery);
               
               if(result.equalsIgnoreCase("error")){            	
               setupUpdate(model, request);	
         		model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
         		request.getSession().setAttribute("error", "Rate already exists for specified date range.");
   				return urlContext + "/form"; 
               }
           }
			beforeSave(request, entity, model);			
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);				
			    if(request.getParameter("typ")!="")   
				return "redirect:/rate/expirationreport/list.do?type=billingrate";
		
			return "redirect:/" + urlContext + "/list.do";
			
			//here
		/*return super.save(request, entity, bindingResult, model);*/
	}
	
	// Billing rate fix - 10thMar2016
	private List<BillingRate> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		
		criteria.setPage(0);
		criteria.setPageSize(100000);
		
		List<BillingRate> billingRateList = genericDAO.search(getEntityClass(), criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(25);
		
		return billingRateList;
	}
	
	@Override
	// Billing rate fix - 10thMar2016
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		List columnPropertyList = (List) request.getSession().getAttribute(
				"columnPropertyList");
		
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html"))
			response.setHeader("Content-Disposition", "attachment;filename="
					+ urlContext + "Report." + type);
		try {
			/*ByteArrayOutputStream out = dynamicReportService.exportReport(
					urlContext + "Report", type, getEntityClass(),
					columnPropertyList, criteria, request);*/
			
			List<BillingRate> billingRateList = searchForExport(model, request);
			ByteArrayOutputStream out = dynamicReportService.exportReport(
					urlContext + "Report", type, getEntityClass(), billingRateList,
					columnPropertyList, request);
			out.writeTo(response.getOutputStream());
			if (type.equals("html"))
				response.getOutputStream()
						.println(
								"<script language=\"javascript\">window.print()</script>");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		}
	}

	
	private String common(HttpServletRequest request,ModelMap model,BillingRate entity,String rateQuery)
	{
		String result="";
		 List<BillingRate> billingrates = genericDAO.executeSimpleQuery(rateQuery);
         
         if (billingrates != null && billingrates.size() > 0) { 
    	   
      	   for(BillingRate billingrate:billingrates){
      	
      	if((entity.getValidFrom().getTime() >= billingrate.getValidFrom().getTime() && entity.getValidTo().getTime()<= billingrate.getValidTo().getTime())
      	||(entity.getValidFrom().getTime() <= billingrate.getValidFrom().getTime() && entity.getValidTo().getTime()>= billingrate.getValidTo().getTime())  
      	|| ((entity.getValidFrom().getTime() >= billingrate.getValidFrom().getTime() && entity.getValidFrom().getTime()<=billingrate.getValidTo().getTime()) && entity.getValidTo().getTime()>= billingrate.getValidTo().getTime())
      	|| (entity.getValidFrom().getTime() <= billingrate.getValidFrom().getTime() && (entity.getValidTo().getTime()>=billingrate.getValidFrom().getTime()   && entity.getValidTo().getTime()<= billingrate.getValidTo().getTime()))){       		
      		    result="error";      		   
      		    return result;
      	}      	
         }   
         }   
         return "";
         }
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if("findDestinationLoad".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("origin"))){
				List<Location> destinatioLoad=new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("transferStation.id", Long.parseLong(request.getParameter("origin")));
				List<BillingRate> billing=genericDAO.findByCriteria(BillingRate.class, criterias, "transferStation", false);
				for(BillingRate bill:billing){
					destinatioLoad.add(bill.getLandfill());
				}
				Gson gson = new Gson();
				return gson.toJson(destinatioLoad);
			}
		}
		if("findOriginLoad".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("destination"))){
				List<Location> originLoad=new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("landfill.id", Long.parseLong(request.getParameter("destination")));
				List<BillingRate> billing=genericDAO.findByCriteria(BillingRate.class, criterias, "landfill", false);
				for(BillingRate bill:billing){
					originLoad.add(bill.getTransferStation());
				}
				Gson gson = new Gson();
				return gson.toJson(originLoad);
			}
		}
		return "";
	}
	
}
