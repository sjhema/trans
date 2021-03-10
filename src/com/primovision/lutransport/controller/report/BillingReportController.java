package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.FuelSurchargePadd;
import com.primovision.lutransport.model.FuelSurchargeWeeklyRate;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.report.Billing;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.service.ReportService;
/**
 * @author Amit
 * 
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/billing")
public class BillingReportController extends ReportController<Billing> {

	public BillingReportController() {
		setReportName("billing");
	}
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String populate(ModelMap model, HttpServletRequest request) {

		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		if (criteria != null) {
			if (criteria.getSearchMap() != null)
				criteria.getSearchMap().clear();
		}
		Map criterias = new HashMap();
		criterias.put("type", 1);
		model.addAttribute("origins",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("name", "Grows");
		criterias.put("type", 2);
		Location grows= genericDAO.getByCriteria(Location.class, criterias);
		criterias.clear();
		criterias.put("type", 2);
		System.out.println("\n grows-->"+grows.getId());
		criterias.put("id!",grows.getId());
		//criterias.put("id!",246l);
		model.addAttribute("destinations",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
		return "reportuser/report/billing";
	}

	//FOR RATE EXPIAED
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		   // System.out.println("\nsearch() of BillingReportController--Start--Type=="+type+"\n");
        System.out.println("\n inside search");
		   Map imagesMap = new HashMap();
		   request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		   populateSearchCriteria(request, request.getParameterMap());
		   SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		 String fromDateStr = (String) searchCriteria.getSearchMap().get("fromDate");
         String toDateStr = (String)searchCriteria.getSearchMap().get("toDate");
         String fromloadDate = (String) searchCriteria.getSearchMap().get("fromloadDate");
         String toloadDate = (String) searchCriteria.getSearchMap().get("toloadDate");
         String fromunloadDate = (String) searchCriteria.getSearchMap().get("fromunloadDate");
         String tounloadDate = (String) searchCriteria.getSearchMap().get("tounloadDate");
         fromDateStr = ReportDateUtil.getFromDate(fromDateStr);
         toDateStr = ReportDateUtil.getToDate(toDateStr);
         fromloadDate = ReportDateUtil.getFromDate(fromloadDate);
         toloadDate	= ReportDateUtil.getToDate(toloadDate);
         fromunloadDate = ReportDateUtil.getFromDate(fromunloadDate);
         tounloadDate	= ReportDateUtil.getToDate(tounloadDate);

         String origin = (String) searchCriteria.getSearchMap().get("origin");
         String destination = (String) searchCriteria.getSearchMap().get("destination");

         StringBuffer query = new StringBuffer("");
        /*query.append("select obj from Ticket obj where obj.status=1 and obj.ticketStatus=1 and obj.billBatch between '"
		+ fromDateStr + "'and '" + toDateStr + "'");*/
         query.append("select obj from Ticket obj where obj.status=1 and obj.ticketStatus=1 ");

        /*	if (!StringUtils.isEmpty(fromDateStr)
		&& !StringUtils.isEmpty(toDateStr)) {
	    query.append("and  obj.billBatch between '" + fromDateStr
 			+ "' and '" + toDateStr + "'");
       }*/

       if (!StringUtils.isEmpty(fromDateStr)) {
	      query.append(" and  obj.billBatch>='").append(fromDateStr + "'");				
        }
       if (!StringUtils.isEmpty(toDateStr)) {
	      query.append(" and  obj.billBatch<='").append(toDateStr + "'");				
        }
       if (!StringUtils.isEmpty(fromloadDate) && !StringUtils.isEmpty(toloadDate)) {
	       query.append("and  obj.loadDate between '" + fromloadDate + "' and '" + toloadDate + "'");
        }
        if (!StringUtils.isEmpty(fromunloadDate)&& !StringUtils.isEmpty(tounloadDate)) {
	        query.append("and  obj.unloadDate between '" + fromunloadDate + "' and '" + tounloadDate + "'");
        }
        if (!StringUtils.isEmpty(origin)) {
	        query.append("and  obj.origin=").append(origin);
         }
        if (!StringUtils.isEmpty(destination)) {
			if(destination.equalsIgnoreCase("91")){
				Map criterias = new HashMap();
				criterias.put("name", "Grows");
				criterias.put("type", 2);
				Location grows= genericDAO.getByCriteria(Location.class, criterias);
				criterias.clear();
				criterias.put("name", "Tullytown");
				criterias.put("type", 2);
				Location tullyTown= genericDAO.getByCriteria(Location.class, criterias);
				
				query.append("and  obj.destination in("+grows.getId()+","+tullyTown.getId()+")");
			}
			else{
				
			query.append("and  obj.destination=").append(destination);
			}
		}
        
        
          String reportname="";
          BillingRate billingRate1 = null;
          FuelSurchargeWeeklyRate fuelSuchargeweekly =  null;
          FuelSurchargePadd fuelSurchargePadd = null;
        
          List<Ticket> tickets=genericDAO.executeSimpleQuery(query.toString()); 
        
 for(Ticket ticket:tickets){
	 billingRate1 = null;
	 Long destination_id;
		Location location=genericDAO.getById(Location.class,ticket.getDestination().getId());
		if(location.getName().equalsIgnoreCase("grows")||location.getName().equalsIgnoreCase("tullytown")){
			
			destination_id=91l;
			
		}
		else{
			destination_id=ticket.getDestination().getId();
		}
		String rateQuery = "select obj from BillingRate obj where obj.transferStation='"
				+ ticket.getOrigin().getId()
				+ "' and obj.landfill='"
				/*+ ticket.getDestination().getId() + "'";*/
				+ destination_id+ "' order by obj.validFrom desc";
	          
	         /* String rateQuery = "select obj from BillingRate obj where obj.transferStation='"
					+ ticket.getOrigin().getId()
					+ "' and obj.landfill='"					
					+ ticket.getDestination().getId()+ "'";*/
	
	         List<BillingRate> fs = genericDAO.executeSimpleQuery(rateQuery);
	         if (fs != null && fs.size() > 0) {	        	 
	        	 reportname=fs.get(0).getBilledby();	        	
	        		for(BillingRate rate:fs) {
	        			
						if (rate.getRateUsing()==null) {
							
							break;
						}
						else if (rate.getRateUsing()==1) {
							//calculation for a load date
							if ((ticket.getLoadDate().getTime()>= rate.getValidFrom().getTime()) && (ticket.getLoadDate().getTime()<= rate.getValidTo().getTime())) {
								billingRate1 = rate;
								break;
							}							
						}
						else if (rate.getRateUsing()==2) {
							//calculation for a unload date
							
							if ((ticket.getUnloadDate().getTime()>= rate.getValidFrom().getTime()) && (ticket.getUnloadDate().getTime()<= rate.getValidTo().getTime())) {
								billingRate1 = rate;
								break;
							}							
						}
					}
	         }	 	        
	         else
	         {	       
	        	 request.setAttribute("error","Rate is Expired or Not available!");
 	         	 return "blank/blank"; 
	         }
	     
	      if (billingRate1 == null) {	    	  
	    	  
	    	  request.setAttribute("error","Rate is Expired or Not available!");
	         	 return "blank/blank"; 	    	  
	      }
	      
	      if (billingRate1 != null) {        	
	        	if(billingRate1.getFuelSurchargeType().equalsIgnoreCase("A")){        		
	        		if(billingRate1.getFuelsurchargeweeklyRate()==1){
	        			String fuelrateQuery = "select obj from FuelSurchargeWeeklyRate obj where obj.transferStations='"
	        				+ billingRate1.getTransferStation().getId()
	        				+ "' and obj.landfillStations='"        			
	        				+ billingRate1.getLandfill().getId()+ "' order by obj.fromDate desc";         
	        	        
	        	
	        	         List<FuelSurchargeWeeklyRate> fsr = genericDAO.executeSimpleQuery(fuelrateQuery);
	        	         if (fsr != null && fsr.size() > 0) {
	        	        	 for(FuelSurchargeWeeklyRate frate:fsr) {
	     	        			
	     						if (billingRate1.getWeeklyRateUsing()==null) {
	     							
	     							break;
	     						}
	     						else if (billingRate1.getWeeklyRateUsing()==1) {
	     							//calculation for a load date
	     							if ((ticket.getLoadDate().getTime()>= frate.getFromDate().getTime()) && (ticket.getLoadDate().getTime()<= frate.getToDate().getTime())) {
	     								fuelSuchargeweekly = frate;
	     								break;
	     							}							
	     						}
	     						else if (billingRate1.getWeeklyRateUsing()==2) {
	     							//calculation for a unload date	     							
	     							if ((ticket.getUnloadDate().getTime()>= frate.getFromDate().getTime()) && (ticket.getUnloadDate().getTime()<= frate.getToDate().getTime())) {
	     								fuelSuchargeweekly = frate;
	     								break;
	     							}							
	     						}
	     						else if (billingRate1.getWeeklyRateUsing()==3){
	     							if ((ticket.getBillBatch().getTime()>= frate.getFromDate().getTime()) && (ticket.getBillBatch().getTime()<= frate.getToDate().getTime())) {
	     								fuelSuchargeweekly = frate;
	     								break;
	     							}	
	     						}	        	        		 
	        	        	 }       	        	 
	        	         }
	        	         else{
	        	        	 request.setAttribute("error","FuelSurcharge Rate is Expired or Not available!");
	         	         	 return "blank/blank"; 
	        	         }
	        	         
	        	         if(fuelSuchargeweekly==null){
	        	        	 request.setAttribute("error","FuelSurcharge Rate is Expired or Not available!");
	         	         	 return "blank/blank"; 
	    	        	 }
	        		}
	        		else if(billingRate1.getFuelsurchargeweeklyRate()==0){	        			 
	        			

	        			String fuelpaddrateQuery = "select obj from FuelSurchargePadd obj where obj.code='"
	        				+ billingRate1.getPadd().getCode()
	        				+ "' order by obj.validFrom desc";         
	        	        
	        	
	        	         List<FuelSurchargePadd> fpr = genericDAO.executeSimpleQuery(fuelpaddrateQuery);
	        	         if (fpr != null && fpr.size() > 0) {
	        	        	 for(FuelSurchargePadd frate:fpr) {
	     	        			
	     						if (billingRate1.getPaddUsing()==null) {
	     							
	     							break;
	     						}
	     						else if (billingRate1.getPaddUsing()==1) {
	     							//calculation for a load date
	     							if ((ticket.getLoadDate().getTime()>= frate.getValidFrom().getTime()) && (ticket.getLoadDate().getTime()<= frate.getValidTo().getTime())) {
	     								fuelSurchargePadd = frate;
	     								break;
	     							}							
	     						}
	     						else if (billingRate1.getPaddUsing()==2) {
	     							//calculation for a unload date	     							
	     							if ((ticket.getUnloadDate().getTime()>= frate.getValidFrom().getTime()) && (ticket.getUnloadDate().getTime()<= frate.getValidTo().getTime())) {
	     								fuelSurchargePadd = frate;
	     								break;
	     							}							
	     						}
	     						else if (billingRate1.getWeeklyRateUsing()==3){
	     							if ((ticket.getBillBatch().getTime()>= frate.getValidFrom().getTime()) && (ticket.getBillBatch().getTime()<= frate.getValidTo().getTime())) {
	     								fuelSurchargePadd = frate;
	     								break;
	     							}	
	     						}	        	        		 
	        	        	 }       	        	 
	        	         }
	        	         else{
	        	        	 request.setAttribute("error","FuelSurcharge Padd Rate is Expired or Not available!");
	         	         	 return "blank/blank"; 
	        	         }
	        	         
	        	         if(fuelSurchargePadd==null){
	        	        	 request.setAttribute("error","FuelSurcharge Padd Rate is Expired or Not available!");
	         	         	 return "blank/blank"; 
	    	        	 }  			
	        			
	        		}       		
	        	}        	
	        }
	      
	      
        }    
 
 
 
        
        
 
          
        
		     if (billingRate1 != null) {
		           int sortBy = (billingRate1.getSortBy() == null) ? 1 : billingRate1.getSortBy();
		           if (sortBy == 1) {
			              query.append(" order by obj.originTicket");
		           } 
		           else {
			              query.append(" order by obj.destinationTicket");
		            }
	         
        } 
        else {
	         query.append(" order by obj.billBatch desc");
        }

        try
        {
			//if(billingRate.getBilledby()!=null)
        	boolean b=false;
        	try
        	{
    			if(billingRate1.getBilledby()!=null){
    				
    			}
    		}catch(Exception e){
    				System.out.println("\nException"+e.getMessage());
    				b=true;
    			}
    		if(b!=true)	
			{   
    			 if(!StringUtils.isEmpty(reportname))
    			 {    				
			           if(reportname.equals("bydestination")){
			    	         setReportName("bydestinationbilling");
			             }
			            if(reportname.equals("byorigin")){
			    	           setReportName("byoriginbilling");
			              }
			             if(reportname.equals("bygallon")){
			    	               setReportName("bygallonbilling");
			              }
			             if(reportname.equals("bydestinationWeight")){
			    	             setReportName("bydestinationWeight");
			              }
			             if(reportname.equals("byoriginWeight")){//byoriginWeight
			    	              setReportName("byoriginWeight");
			               }
			              if(reportname.equals("bynoFuelSurchage")){
			    	            setReportName("bynoFuelSurchage");
			                }
			               if(reportname.equals("bynoTonnage")){
				                 setReportName("bynoTonnage");
			                }
			               // Peak rate 2nd Feb 2021
			               if(reportname.equals("byPeakRate")){
			               	setReportName("byPeakRateBilling");
			               }
			     }
				 else
				 {
					  setReportName("billing");
				 }
			}
			else{
				setReportName("billing");
			}
    		Map<String,Object> datas = generateData(searchCriteria, request);
			//List propertyList = generatePropertyList(request);
			//request.getSession().setAttribute("propertyList", propertyList);
			if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + reportName + "." + type);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			out = dynamicReportService.generateStaticReport(reportName,
					(List)datas.get("data"), params, type, request);
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}

	}
	
	
	//
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		//*************************RRRRRR
	  String fromDateStr = (String) searchCriteria.getSearchMap().get("fromDate");
      String toDateStr = (String)searchCriteria.getSearchMap().get("toDate");
      String fromloadDate = (String) searchCriteria.getSearchMap().get("fromloadDate");
      String toloadDate = (String) searchCriteria.getSearchMap().get("toloadDate");
      String fromunloadDate = (String) searchCriteria.getSearchMap().get("fromunloadDate");
      String tounloadDate = (String) searchCriteria.getSearchMap().get("tounloadDate");
      fromDateStr = ReportDateUtil.getFromDate(fromDateStr);
      toDateStr = ReportDateUtil.getToDate(toDateStr);
      fromloadDate = ReportDateUtil.getFromDate(fromloadDate);
      toloadDate	= ReportDateUtil.getToDate(toloadDate);
      fromunloadDate = ReportDateUtil.getFromDate(fromunloadDate);
      tounloadDate	= ReportDateUtil.getToDate(tounloadDate);

      String origin = (String) searchCriteria.getSearchMap().get("origin");
      String destination = (String) searchCriteria.getSearchMap().get("destination");

      StringBuffer query = new StringBuffer("");
     /*query.append("select obj from Ticket obj where obj.status=1 and obj.ticketStatus=1 and obj.billBatch between '"
		+ fromDateStr + "'and '" + toDateStr + "'");*/
      query.append("select obj from Ticket obj where obj.status=1 and obj.ticketStatus=1 ");

     /*	if (!StringUtils.isEmpty(fromDateStr)
		&& !StringUtils.isEmpty(toDateStr)) {
	    query.append("and  obj.billBatch between '" + fromDateStr
			+ "' and '" + toDateStr + "'");
    }*/

    if (!StringUtils.isEmpty(fromDateStr)) {
	      query.append(" and  obj.billBatch>='").append(fromDateStr + "'");				
     }
    if (!StringUtils.isEmpty(toDateStr)) {
	      query.append(" and  obj.billBatch<='").append(toDateStr + "'");				
     }
    if (!StringUtils.isEmpty(fromloadDate) && !StringUtils.isEmpty(toloadDate)) {
	       query.append("and  obj.loadDate between '" + fromloadDate + "' and '" + toloadDate + "'");
     }
     if (!StringUtils.isEmpty(fromunloadDate)&& !StringUtils.isEmpty(tounloadDate)) {
	        query.append("and  obj.unloadDate between '" + fromunloadDate + "' and '" + tounloadDate + "'");
     }
     if (!StringUtils.isEmpty(origin)) {
	        query.append("and  obj.origin=").append(origin);
      }
     if (!StringUtils.isEmpty(destination)) {
	        query.append("and  obj.destination=").append(destination);
	     }
     
     
     BillingRate billingRate = null;
     System.out.println("\norigin--->"+origin+"\n");
     System.out.println("\ndestination--->"+destination+"\n");
     if (!StringUtils.isEmpty(origin) && !StringUtils.isEmpty(destination)) {
	         String rateQuery = "select obj from BillingRate obj where obj.transferStation="
			 + origin + " and obj.landfill=" + destination+" order by obj.validFrom desc";
	
	         List<BillingRate> fs = genericDAO.executeSimpleQuery(rateQuery);
	         
	
	         if (fs != null && fs.size() > 0) {
		            billingRate = fs.get(0);
		           /* for(int i=0;i<fs.size();i++)
		            {
		               //System.out.println("\nrateSize===>"+i+"==>"+fs.get(i).getId()+"\n");
		               if(i==fs.size()-1)
		               {
		            	   billingRate = fs.get(i); 
		               }
		            }*/
	          }
     } 
     else {
	         query.append(" order by obj.billBatch desc");
     }
    
		
		try {
			boolean b=false;
        	try
        	{
    			if(billingRate.getBilledby()!=null){
    				
    			}
    		}catch(Exception e){
    				//System.out.println("\nException\n");
    				b=true;
    			}
    		if(b!=true)	
			{   
    		  if(!StringUtils.isEmpty(billingRate.getBilledby()))
    		  { 
			           if(billingRate.getBilledby().equals("bydestination")){
			        	   if (StringUtils.isEmpty(type)){
	            		          setReportName("bydestinationbilling");
	            	          }
			    	         if (!StringUtils.isEmpty(type))
			    	         {
			    	             if (type.equals("pdf")){
	            		            setReportName("bydestinationbillingpdf");
	            	              }
			    	              else{
			    		             setReportName("bydestinationbilling");
			    	               }
			    	         }
			           }
			           if(billingRate.getBilledby().equals("byorigin")){
				           //setReportName("byoriginbilling");
			        	   if (StringUtils.isEmpty(type)){
	            		          setReportName("byoriginbilling");
	            	          }
			    	         if (!StringUtils.isEmpty(type))
			    	         {
			    	             if (type.equals("pdf")){
	            		            setReportName("byoriginbillingpdf");
	            	              }
			    	              else{
			    		             setReportName("byoriginbilling");
			    	               }
			    	         }
			           }
			           if(billingRate.getBilledby().equals("bygallon"))
			           {
			    	         if (StringUtils.isEmpty(type)){
	            		          setReportName("bygallonbilling");
	            	          }
			    	         if (!StringUtils.isEmpty(type))
			    	         {
			    	             if (type.equals("pdf")){
	            		             System.out.println("BRC--bygallonbillingpdf");
	            		            setReportName("bygallonbillingpdf");
	            	              }
			    	              else{
			    		             setReportName("bygallonbilling");
			    	               }
			    	         }
			           }
			    
			           if(billingRate.getBilledby().equals("bydestinationWeight")){
				             //setReportName("bydestinationWeight");
			        	   if (StringUtils.isEmpty(type)){
	            		          setReportName("bydestinationWeight");
	            	          }
			    	         if (!StringUtils.isEmpty(type))
			    	         {
			    	             if (type.equals("pdf")){
	            		            setReportName("bydestinationWeightpdf");
	            	              }
			    	              else{
			    		             setReportName("bydestinationWeight");
			    	               }
			    	         }
			           }
			           if(billingRate.getBilledby().equals("byoriginWeight")){
				              //setReportName("byoriginWeight");
			        	   if (StringUtils.isEmpty(type)){
	            		          setReportName("byoriginWeight");
	            	          }
			    	         if (!StringUtils.isEmpty(type))
			    	         {
			    	             if (type.equals("pdf")){
	            		            setReportName("byoriginWeightpdf");
	            	              }
			    	              else{
			    		             setReportName("byoriginWeight");
			    	               }
			    	         }
			           }
			           if(billingRate.getBilledby().equals("bynoFuelSurchage")){
				               //setReportName("bynoFuelSurchage");
			        	   if (StringUtils.isEmpty(type)){
	            		          setReportName("bynoFuelSurchage");
	            	          }
			    	         if (!StringUtils.isEmpty(type))
			    	         {
			    	             if (type.equals("pdf")){
	            		            setReportName("bynoFuelSurchagepdf");
	            	              }
			    	              else{
			    		             setReportName("bynoFuelSurchage");
			    	               }
			    	         }
			            }
			           if(billingRate.getBilledby().equals("bynoTonnage")){
				              //setReportName("bynoTonnage");
			        	   if (StringUtils.isEmpty(type)){
	            		          setReportName("bynoTonnage");
	            	          }
			    	         if (!StringUtils.isEmpty(type))
			    	         {
			    	             if (type.equals("pdf")){
	            		            setReportName("bynoTonnagepdf");
	            	              }
			    	              else{
			    		             setReportName("bynoTonnage");
			    	               }
			    	         }
			            }
			   }
    		   else
    		   {
    			   if (type.equals("pdf")){
    		        setReportName("billingpdf");
    		        }
    			   else{
    				   setReportName("billing"); 
    			   }
    		   }
			}
			else
			{
				 if (type.equals("pdf")){
	    		        setReportName("billingpdf");
	    		        }
	    			   else{
	    				   setReportName("billing"); 
	    			   }
			}
			
			
			Map<String,Object> datas = generateData(searchCriteria, request);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + reportName + "." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")) {
				out = dynamicReportService.generateStaticReport(reportName,
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport(reportName+"print",
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	
	
	
	
	@Override
	protected Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request) {
		Map<String,Object> data = new HashMap<String,Object>();
		Map params = new HashMap();
		if (criteria.getSearchMap().get("fromDate")!=null)
			params.put("fromDate", criteria.getSearchMap().get("fromDate"));
		if (criteria.getSearchMap().get("toDate")!=null)
			params.put("toDate", criteria.getSearchMap().get("toDate"));
		if (criteria.getSearchMap().get("fromloadDate")!=null)
			params.put("loadDate", criteria.getSearchMap().get("fromloadDate"));
		if (criteria.getSearchMap().get("toloadDate")!=null)
			params.put("loadDate", criteria.getSearchMap().get("toloadDate"));
		if (criteria.getSearchMap().get("fromunloadDate")!=null)
			params.put("unloadDate", criteria.getSearchMap().get("fromunloadDate"));
		if (criteria.getSearchMap().get("tounloadDate")!=null)
			params.put("unloadDate", criteria.getSearchMap().get("tounloadDate"));
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("origin"))) {
			Location origin = genericDAO.getById(Location.class,Long.parseLong((String)criteria.getSearchMap().get("origin")));
			params.put("origin", origin.getName());
		}
		if (!StringUtils.isEmpty((String)criteria.getSearchMap().get("destination"))) {
			Location destination = genericDAO.getById(Location.class,Long.parseLong((String)criteria.getSearchMap().get("destination")));
			params.put("destination", destination.getName());
		}
		if (criteria.getSearchMap().get("invoiceDate")!=null) {
			params.put("date", (String)criteria.getSearchMap().get("invoiceDate"));
		}
		else {
			params.put("date", new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime()));
		}
		params.put("invoiceNo", (String)criteria.getSearchMap().get("invoiceNumber"));
		BillingWrapper wrapper = generateBillingReport(criteria);
		params.put("sumBillableTon", wrapper.getSumBillableTon());
		params.put("totalRowCount",wrapper.getTotalRowCount());
		params.put("sumOriginTon", wrapper.getSumOriginTon());
		params.put("sumDestinationTon", wrapper.getSumDestinationTon());
		params.put("sumTonnage", wrapper.getSumTonnage());
		// Loading fee changes 10th Mar 2021
		params.put("sumLoadingFee", wrapper.getSumLoadingFee());
		params.put("sumTotal", wrapper.getSumTotal());
		params.put("sumDemurrage", wrapper.getSumDemmurage());
		params.put("sumNet", wrapper.getSumNet());
		params.put("sumAmount", wrapper.getSumAmount());
		params.put("sumFuelSurcharge", wrapper.getSumFuelSurcharge());
		params.put("sumGallon", wrapper.getSumGallon());
		data.put("data", wrapper.getBilling());
		data.put("params",params);
		return data;
	}

	public BillingWrapper generateBillingReport(SearchCriteria searchCriteria) {
		return reportService.generateBillingData(searchCriteria);
	}
	
	
	/**
	 * Generic method to export report based on the filter criteria.
	 * 
	 * @param type
	 *            File type to which the report has to be exported
	 * @param jrxml
	 *            To decide whether jrxml or dynamicreport's method is to be
	 *            used
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/save.do")
	public String save(ModelMap model, HttpServletRequest request,	HttpServletResponse response) {
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		try {			
			reportService.saveBillingData(request,criteria);
			request.getSession().setAttribute("msg", "Your invoice is saved successfully");
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
		return "blank/blank";
	}
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model)
	{
		List rate_notes=new ArrayList();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

		String notes="";
		if ("findNote".equalsIgnoreCase(action)) 
		{
			
			Map criterias = new HashMap();

			String fromDateStr = ReportDateUtil.getFromDate(request.getParameter("fromDate"));
			String toDateStr = ReportDateUtil.getToDate(request.getParameter("toDate"));

			String fromloadDateStr = ReportDateUtil.getFromDate(request.getParameter("fromloadDate"));
			String toloadDateStr = ReportDateUtil.getToDate(request.getParameter("toloadDate"));

			String fromunloadDateStr = ReportDateUtil.getFromDate(request.getParameter("fromunloadDate"));
			String tounloadDateStr = ReportDateUtil.getToDate(request.getParameter("tounloadDate"));


			try 
			{

				criterias.put("transferStation.id", Long.parseLong(request.getParameter("origin")));
				criterias.put("landfill.id", Long.parseLong(request.getParameter("destination")));

				List<BillingRate> billingrate = genericDAO.findByCriteria(BillingRate.class, criterias,"validFrom",false);


				StringBuffer query=new StringBuffer();

				query.append("select loadDate,unloadDate from Ticket t where t.status=1 ");

				if(!StringUtils.isEmpty(request.getParameter("origin")) && !StringUtils.isEmpty(request.getParameter("destination")))
				{
					int flag=0;

					query.append(" and t.origin='"+Long.parseLong(request.getParameter("origin"))+ "'");
					query.append(" and t.destination='"+Long.parseLong(request.getParameter("destination"))+ "'");


					if(!StringUtils.isEmpty(request.getParameter("fromDate")))
					{
						flag=1;
						query.append(" and t.billBatch>='"+fromDateStr+ "'");

					}

					if(!StringUtils.isEmpty(request.getParameter("toDate")))
					{
						flag=1;
						query.append(" and t.billBatch<='"+toDateStr+ "'");
					}

					if(!StringUtils.isEmpty(request.getParameter("fromloadDate")))
					{
						flag=1;
						query.append(" and t.loadDate>='"+fromloadDateStr+ "'");
					}

					if(!StringUtils.isEmpty(request.getParameter("toloadDate")))
					{
						flag=1;
						query.append(" and t.loadDate<='"+toloadDateStr+ "'");
					}

					if(!StringUtils.isEmpty(request.getParameter("fromunloadDate")))
					{
						flag=1;
						query.append(" and t.unloadDate>='"+fromunloadDateStr+ "'");
					}

					if(!StringUtils.isEmpty(request.getParameter("tounloadDate")))
					{
						flag=1;
						query.append(" and t.unloadDate<='"+tounloadDateStr+ "'");
					}

					if(flag==1)
					{	

						List<Ticket> tick = genericDAO.executeSimpleQuery(query.toString());
						
						if(tick!=null && !tick.isEmpty())
						{
							for(Object obj: tick){
								Object[] objs=(Object[])obj;

								String ldate=dateFormat.format(objs[0]);
								String udate=dateFormat.format(objs[1]);
								Date  load_date=dateFormat.parse(ldate);
								Date unload_date=dateFormat.parse(udate);									
								if(billingrate!=null && !billingrate.isEmpty())
								{
									for(BillingRate br: billingrate)
									{						
										if(br.getValidFrom().getTime()<=load_date.getTime() && br.getValidTo().getTime()>=unload_date.getTime())
										{											
											if(!br.getNotes().equals(""))
											{
												if(rate_notes==null && rate_notes.size()<=0)
												{
													rate_notes.add(br.getNotes());
												}
												else
												{
													if(!rate_notes.contains(br.getNotes()))
													{
														rate_notes.add(br.getNotes());
													}
												}
											}
										}	
									}
								}
							}
						}
					}
				}								
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			for(int i=0; i<rate_notes.size();i++)
			{   if(i==0)
				notes=(String) rate_notes.get(i);
			else
				notes=notes+"<br />"+rate_notes.get(i);
			}
			Gson gson = new Gson();				
			return gson.toJson(notes);
			
		}

		if("findDestinationLoad".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("origin"))){
				List<Location> destinatioLoad=new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("transferStation.id", Long.parseLong(request.getParameter("origin")));
				List<BillingRate> billing=genericDAO.executeSimpleQuery("select obj from BillingRate obj where obj.transferStation="+Long.parseLong(request.getParameter("origin"))+" group by landfill order by landfill.name");
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
				List<BillingRate> billing=genericDAO.executeSimpleQuery("select obj from BillingRate obj where obj.landfill="+Long.parseLong(request.getParameter("destination"))+" group by transferStation order by transferStation.name");
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
