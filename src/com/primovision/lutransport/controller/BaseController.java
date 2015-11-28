package com.primovision.lutransport.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.google.gson.Gson;
import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.Language;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.service.AuditService;

@SuppressWarnings("unchecked")
public class BaseController {
	protected static DecimalFormat decimalFormat = new DecimalFormat(
			"######.000");

	protected static Logger log = Logger
			.getLogger("com.primovision.lutransport.controller");

	@Autowired
	private AuditService auditService;

	protected String urlContext;

	public String getUrlContext() {
		return urlContext;
	}

	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
	}

	// Set up any custom editors, adds a custom one for java.sql.date by default
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	@Autowired
	protected GenericDAO genericDAO;

	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	@Autowired
	protected Validator validator;

	/**
	 * @return the validator
	 */
	public Validator getValidator() {
		return validator;
	}

	/**
	 * @param validator
	 *            the validator to set
	 */
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	protected User getUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute("userInfo");
	}

	protected List<StaticData> listStaticData(String staticDataType) {
		Map criteria = new HashMap();
		criteria.put("dataType", staticDataType);
		return genericDAO.findByCriteria(StaticData.class, criteria);
	}

	protected void setLanguageAttributes(HttpServletRequest request,
			Language language) {
		request.getSession().setAttribute("lang", language.getLocale());
		if (language.getRtl() == true) {
			request.getSession().setAttribute("dir", "rtl");
			request.getSession().setAttribute("right", "left");
			request.getSession().setAttribute("left", "right");
		} else {
			request.getSession().setAttribute("dir", "ltr");
			request.getSession().setAttribute("right", "right");
			request.getSession().setAttribute("left", "left");
		}
	}

	protected void populateSearchCriteria(HttpServletRequest request,
			Map<String, String[]> params) {

		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		if (criteria == null) {
			criteria = new SearchCriteria();
			criteria.setPageSize(25);
		}
		if (!StringUtils.isEmpty(request.getParameter("rst"))) {
			criteria.getSearchMap().clear();
			if(request.getSession().getAttribute("unitval")!=null){
				request.getSession().removeAttribute("unitval");
			}
			if(request.getSession().getAttribute("inspectionDriverIdlist")!=null){
				request.getSession().removeAttribute("inspectionDriverIdlist");
			}
			if(request.getSession().getAttribute("fuelogverificationData")!=null){
				request.getSession().removeAttribute("fuelogverificationData");
			}
		}
		criteria.setRequestParams(params);
		if (params != null && params.size()>0) {
			Map parameters = new HashMap();
			if (params.get("pageSize") != null) {
				criteria.setPageSize(Integer.parseInt(params.get("pageSize")[0]));
			}
			if (params.get("search") == null
					&& request.getParameter("p") != null) {
				criteria.setPage(Integer.parseInt(request.getParameter("p")));
			} else {
				if (params.size() > 0) {
					if (params.get("queryString") != null) {
						parameters.put(params.get("searchBy")[0],
								params.get("queryString")[0]);
					} else {
						Object[] keys = params.keySet().toArray();
						for (int i = 0; i < keys.length; i++) {
							if (params.get(keys[i]) != null) {
								if (!"rst".equalsIgnoreCase(keys[i].toString()))
									parameters.put(keys[i],
											params.get(keys[i])[0]);
							}
						}
					}
				}
				criteria.setPage(0);
				criteria.setSearchMap(parameters);
			}
		}
		request.getSession().setAttribute("searchCriteria", criteria);
	}

	protected String getCriteriaAsString(SearchCriteria criteria) {
		StringBuffer buffer = new StringBuffer("");
		if (criteria != null) {
			Iterator<String> it = criteria.getSearchMap().keySet().iterator();
			String key = null;
			String value = null;
			while (it.hasNext()) {
				key = it.next();
				value = (String) criteria.getSearchMap().get(key);
				if (!StringUtils.isEmpty(value)) {
					buffer.append(key + ":" + value).append(" ");
				}
			}
		}
		return buffer.toString();
	}

	public void writeActivityLog(String activityType, String details) {
		auditService.writeActivityLog(urlContext, activityType, details);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/ajax.do")
	public @ResponseBody
	String ajaxRequest(HttpServletRequest request,
			@RequestParam(value = "action", required = true) String action,
			Model model) {
		return processAjaxRequest(request, action, model);
	}

	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		return gson.toJson("");
	}
}
