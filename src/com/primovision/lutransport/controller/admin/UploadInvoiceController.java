package com.primovision.lutransport.controller.admin;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.core.configuration.Configuration;
import com.primovision.lutransport.core.util.MathUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Invoice;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.report.Billing;
import com.primovision.lutransport.service.DateUpdateService;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@RequestMapping("/admin/uploadinvoice")
public class UploadInvoiceController extends CRUDController<Invoice> {
	
	public UploadInvoiceController() {
		setUrlContext("admin/uploadinvoice");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		criterias.put("type", 1);
		model.addAttribute("transferStations", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfills", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	
	@ModelAttribute("modelObject")
	public Invoice setupModel(HttpServletRequest request) {
		String pk = request.getParameter(getPkParam());
		if (pk == null || org.apache.commons.lang.StringUtils.isEmpty(pk)) {
			Invoice invoice = getEntityInstance();
			invoice.setInvoiceNumber(new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
		return invoice;
		} else {
			return genericDAO.getById(getEntityClass(), Long.parseLong(pk));
		}
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "InvoiceDate", "invoiceDate");
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria));
		//criteria.getSearchMap().put("InvoiceDate", request.getParameter("InvoiceDate"));
		return urlContext + "/list";
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Invoice entity,
			BindingResult bindingResult, ModelMap model) {
		// TODO Auto-generated method stub
		if (entity.getTransferStation() == null) {
			bindingResult.rejectValue("transferStation", "error.select.option",
					null, null);
		}
		if (entity.getLandfill() == null) {
			bindingResult.rejectValue("landfill", "error.select.option",
					null, null);
		}
		// validate entity
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		String path=Configuration.getProperty("storage.folder.repository")+"/"+"invoice";
		CommonsMultipartFile file=entity.getInvoiceFile();
		String invoicefileName=file.getOriginalFilename();
		File folderpath=new File(path);
		try{
			if(!folderpath.exists()){
				folderpath.mkdirs();
			}
			String fullpath=path+"/"+invoicefileName;
			System.out.println(fullpath);
			File invoicefile=new File(fullpath);
			if(!invoicefile.exists()){
				invoicefile.createNewFile();
			}
			file.transferTo(invoicefile);
			entity.setFilePath(fullpath);
			beforeSave(request, entity, model);
			// merge into datasource
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
		}catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:list.do";
	}
	
	@RequestMapping(value="/downloadinvoice.do")
	public String downlodinvoice(HttpServletRequest request,Model model,HttpServletResponse response){
		String invoiceId=request.getParameter("id");
		String type=request.getParameter("type");
		Invoice invoice=genericDAO.getById(Invoice.class, Long.valueOf(invoiceId));
		
		try {	
			
			String rateQuery1=null;	       
	        rateQuery1 = "select obj from BillingRate obj where obj.transferStation="
				                     + invoice.getTransferStation().getId() + " and obj.landfill=" + invoice.getLandfill().getId();
		    
	        List<BillingRate> fs = genericDAO.executeSimpleQuery(rateQuery1);
			
			BillingRate billingRate = null;
			if (fs != null && fs.size() > 0) {
				billingRate = fs.get(0);
			}			
			
			Map params = new HashMap();
			List<Billing> datas=null;
			params.put("invoiceNo", invoice.getInvoiceNumber());
			params.put("origin", invoice.getTransferStation().getName());
			params.put("destination",invoice.getLandfill().getName());
			
			if(invoice.getLandfill().getName().equalsIgnoreCase("Grows/Tullytown")){
				Location grows = null;
				Location tullyTown=null;
				
					Map criterias = new HashMap();
					criterias.put("name", "Grows");
					criterias.put("type", 2);
					grows= genericDAO.getByCriteria(Location.class, criterias);
					criterias.clear();
					criterias.put("name", "Tullytown");
					criterias.put("type", 2);
					tullyTown= genericDAO.getByCriteria(Location.class, criterias);
					StringBuffer query=new StringBuffer(" select obj from Billing obj where obj.invoiceNo='"
						+invoice.getInvoiceNumber()+"' and obj.origin='"+invoice.getTransferStation().getName()
						+"' and obj.destination in ('"+grows.getName()+"','"+tullyTown.getName()+"')");
					if (billingRate != null) {
						int sortBy = (billingRate.getSortBy() == null) ? 1
								: billingRate.getSortBy();
						if (sortBy == 1) {
							query.append(" order by obj.originTicket");
						} else {
							query.append(" order by obj.destinationTicket");
						}
					}
					else{
						query.append(" order by obj.originTicket");
					}
				
					datas=genericDAO.executeSimpleQuery(query.toString());
			}		
			else{	
			params.put("invoiceNo", invoice.getInvoiceNumber());
			params.put("origin", invoice.getTransferStation().getName());
			params.put("destination",invoice.getLandfill().getName());			
			if (billingRate != null) {
				int sortBy = (billingRate.getSortBy() == null) ? 1
						: billingRate.getSortBy();
				if (sortBy == 1) {
					datas= genericDAO.executeSimpleQuery("select obj from Billing obj where obj.invoiceNo='"+invoice.getInvoiceNumber()+"' and origin='"+invoice.getTransferStation().getName()+"' and destination='"+invoice.getLandfill().getName()+"' order by originTicket asc");
					//datas = genericDAO.findByCriteria(Billing.class, params,"originTicket",false);
				} else {
					datas= genericDAO.executeSimpleQuery("select obj from Billing obj where obj.invoiceNo='"+invoice.getInvoiceNumber()+"' and origin='"+invoice.getTransferStation().getName()+"' and destination='"+invoice.getLandfill().getName()+"' order by destinationTicket asc");
					//datas = genericDAO.findByCriteria(Billing.class, params,"destinationTicket",false);
				}
			}
			else{
				datas= genericDAO.executeSimpleQuery("select obj from Billing obj where obj.invoiceNo='"+invoice.getInvoiceNumber()+"' and origin='"+invoice.getTransferStation().getName()+"' and destination='"+invoice.getLandfill().getName()+"' order by originTicket asc");
				//datas = genericDAO.findByCriteria(Billing.class, params,"originTicket",false);
			}		
		}
			if (datas!=null && datas.size()>0) {
				params.clear();
				//Location origin = genericDAO.getById(Location.class,Long.parseLong(datas.get(0).getOrigin()));
				params.put("origin", datas.get(0).getOrigin());
				//Location destination = genericDAO.getById(Location.class,Long.parseLong(datas.get(0).getDestination()));
				params.put("destination",invoice.getLandfill().getName());
				//params.put("destination", destination.getName());
				
				params.put("date", new SimpleDateFormat("MM-dd-yyyy").format(invoice.getInvoiceDate()));
				params.put("invoiceNo", datas.get(0).getInvoiceNo());
				params.put("sumBillableTon", invoice.getSumBillableTon());
				params.put("sumOriginTon", invoice.getSumOriginTon());
				params.put("sumDestinationTon", invoice.getSumDestinationTon());
				params.put("sumTonnage", invoice.getSumTonnage());				
				params.put("sumTotal", invoice.getSumTotal());
				params.put("sumDemurrage", invoice.getSumDemmurage());
				params.put("sumNet", invoice.getSumNet());
				params.put("sumAmount", invoice.getSumAmount());
				params.put("sumFuelSurcharge", invoice.getSumFuelSurcharge());				
				
				
				if(invoice.getSumGallon()==null){
					double sumgallon=0.0;
					for(Billing billing: datas){
						sumgallon+=billing.getGallon();					
					}	
					sumgallon=MathUtil.roundUp(sumgallon, 2);
					System.out.println("****** the value is "+sumgallon);
					params.put("sumGallon",sumgallon);
					
					Map criterias = new HashMap();
					criterias.clear();
					criterias.put("name",datas.get(0).getOrigin());
					Location origin=genericDAO.getByCriteria(Location.class, criterias);
					criterias.clear();
					criterias.put("name",datas.get(0).getDestination());
					Location destination=genericDAO.getByCriteria(Location.class, criterias);					
					String queryInvoice = "select obj from Invoice obj where obj.invoiceNumber='"
						+datas.get(0).getInvoiceNo()+"' and obj.transferStation='"
						+origin.getId()+"' and obj.landfill='"
						+destination.getId()+"'"; 
			    	
			    	
			    	
			    	List<Invoice> actlInv = genericDAO.executeSimpleQuery(queryInvoice);
					
				    if(!actlInv.isEmpty() && actlInv.size()>0){				    	
				    	for(Invoice invobj:actlInv ){				    		
				    		if(invobj.getSumGallon()==null){
				    			
				    			invobj.setSumGallon(sumgallon);
				    			genericDAO.saveOrUpdate(invobj);
				    		}				    	
				    	}				    	
				    }				
				}
				else{			
				    params.put("sumGallon",invoice.getSumGallon());
				
			    }
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
				//for 7 invoice format
				//String rateQuery1=null;
		        BillingRate billingRate1 = null;
		        /*rateQuery1 = "select obj from BillingRate obj where obj.transferStation="
					                     + invoice.getTransferStation().getId() + " and obj.landfill=" + invoice.getLandfill().getId();
			   System.out.println("***** the query is "+rateQuery1.toString());
		        List<BillingRate> fs = genericDAO.executeSimpleQuery(rateQuery1);*/
			    //System.out.println("\nrateSize===>"+fs.size()+"\n");
			    if (fs != null && fs.size() > 0) 
			    {
				     billingRate1 = fs.get(0);
				     for(int i=0;i<fs.size();i++)
				     {
				               //System.out.println("\nrateSize===>"+i+"==>"+fs.get(i).getId()+"\n");
				               if(i==fs.size()-1)
				               {
				            	   billingRate1 = fs.get(i); 
				               }
				     }
				}
			    
			    try
		        {
			    	 boolean b=false;
		        	 try
		        	 {
		    			if(billingRate1.getBilledby()!=null){
		    		     }
		    		 }catch(Exception e){
		    				b=true;
		    		 }
		    		 if(!StringUtils.isEmpty(billingRate1.getBilledby()) && b!=true)
		    		 {
				              /*if((billingRate1.getBilledby()).length()!=0 && (billingRate1.getBilledby()!=null))
					           {*/
					            if(billingRate1.getBilledby().equals("bydestination")){
					            	if (type.equals("pdf")){
					            		System.out.println("bydestinationbillingpdf");
					            		out = dynamicReportService.generateStaticReport("bydestinationbillingpdf",datas, params, type, request);
					            	}else{
						                 out = dynamicReportService.generateStaticReport("bydestinationbilling",datas, params, type, request);
					            	}
					            }
					            if(billingRate1.getBilledby().equals("byorigin")){
					            	if (type.equals("pdf")){
					            		System.out.println("byoriginbillingpdf");
					            		out = dynamicReportService.generateStaticReport("byoriginbillingpdf",datas, params, type, request);
					            	}
					            	else{
						                 out = dynamicReportService.generateStaticReport("byoriginbilling",datas, params, type, request);
					            	}
					            }
					            if(billingRate1.getBilledby().equals("bygallon")){
					            	if (type.equals("pdf")){
					            		System.out.println("bygallonbillingpdf");
					            		out = dynamicReportService.generateStaticReport("bygallonbillingpdf",datas, params, type, request);
					            	}
					            	else{
						                 out = dynamicReportService.generateStaticReport("bygallonbilling",datas, params, type, request);
					            	}
					            }
					            if(billingRate1.getBilledby().equals("bydestinationWeight")){
					            	if (type.equals("pdf")){
					            		System.out.println("bydestinationWeightpdf");
					            		out = dynamicReportService.generateStaticReport("bydestinationWeightpdf",datas, params, type, request);
					            	}
					            	else{
						                 out = dynamicReportService.generateStaticReport("bydestinationWeight",datas, params, type, request);
					            	}
					            }
					            if(billingRate1.getBilledby().equals("byoriginWeight")){
					            	if (type.equals("pdf")){
					            		System.out.println("byoriginWeightpdf");
					            		out = dynamicReportService.generateStaticReport("byoriginWeightpdf",datas, params, type, request);
					            	}
					            	else{
						                 out = dynamicReportService.generateStaticReport("byoriginWeight",datas, params, type, request);
					            	}
					            }
					            if(billingRate1.getBilledby().equals("bynoFuelSurchage")){
					            	if (type.equals("pdf")){
					            		System.out.println("bynoFuelSurchagepdf");
					            		out = dynamicReportService.generateStaticReport("bynoFuelSurchagepdf",datas, params, type, request);
					            	}
					            	else{
						                 out = dynamicReportService.generateStaticReport("bynoFuelSurchage",datas, params, type, request);
					            	}
					            }
					            if(billingRate1.getBilledby().equals("bynoTonnage")){
					            	if (type.equals("pdf")){
					            		System.out.println("bynoTonnagepdf");
					            		out = dynamicReportService.generateStaticReport("bynoTonnagepdf",datas, params, type, request);
					            	}
					            	else{
						                 out = dynamicReportService.generateStaticReport("bynoTonnage",datas, params, type, request);
					            	}
					            }
					}
					else
					{
						System.out.println("\nbilledBy is empti\n");
						System.out.println("\nbilledBy  else===>"+billingRate1.getBilledby()+"\n");
						if (type.equals("pdf")){
							out = dynamicReportService.generateStaticReport("billingpdf",
								datas, params, type, request);
							}
							else{
								out = dynamicReportService.generateStaticReport("billing",
										datas, params, type, request);
							}
							
					}
				    response.setHeader("Content-Disposition",
							"attachment;filename=inv" + invoice.getInvoiceNumber() + "."+type);
				    out.writeTo(response.getOutputStream());
				    out.close();
					return null;
					
		        } 
			    catch (Exception e) 
			    {
					e.printStackTrace();
					request.getSession().setAttribute("errors", e.getMessage());
					return "error";
				}
				
				//for 7 invoice format
				
				/*if (type.equals("pdf")){
				out = dynamicReportService.generateStaticReport("billingpdf",
					datas, params, type, request);
				}
				else{
					out = dynamicReportService.generateStaticReport("billing",
							datas, params, type, request);
				}
				response.setHeader("Content-Disposition",
					"attachment;filename=inv" + invoice.getInvoiceNumber() + "."+type);
				out.writeTo(response.getOutputStream());
				out.close();
				return null;*/
			}
			else {
				request.getSession().setAttribute("errors", "Unable to download invoice. Plesae try again later");
				return "error";
			}
		}catch(Exception ex){
			ex.printStackTrace();
			request.getSession().setAttribute("errors", "Unable to download invoice. Plesae try again later");
			return "error";
		}
	}
	
	
	@Override
	public String delete(@ModelAttribute("modelObject") Invoice entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			Invoice invoice=genericDAO.getById(Invoice.class, Long.valueOf(entity.getId()));
			reportService.deleteBillingData(invoice);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}
	
	
	public ByteArrayOutputStream downloadattachment(String path,
			HttpServletRequest request) {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			FileInputStream fis=new FileInputStream(new File(path));
			BufferedInputStream bis=new BufferedInputStream(fis);
			int i;
			while((i=bis.read())!=-1){
				baos.write(i);
			}
			fis.close();
			bis.close();;
		} catch (Exception e) {	
			e.printStackTrace(); 
		}
		return baos;
	}
	
}
