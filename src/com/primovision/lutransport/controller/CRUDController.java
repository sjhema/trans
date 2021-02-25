package com.primovision.lutransport.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.AbstractBaseModel;
import com.primovision.lutransport.model.BaseModel;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.service.DynamicReportService;

@SuppressWarnings("unchecked")
public abstract class CRUDController<T extends BaseModel> extends
		BaseController {
	
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}

	protected String urlContext;

	@Override
	public String getUrlContext() {
		return urlContext;
	}

	@Override
	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
	}

	public CRUDController() {
		super();
		setUrlContext(ClassUtils.getShortName(getEntityClass()).toLowerCase());
	}

	protected Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected T getEntityInstance() {
		try {
			return (T) Class.forName(getEntityClass().getName()).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	// assume the primary key property is going to be the Entity Class plus Seq
	protected String getPkParam() {
		return "id";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/create.do")
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		return urlContext + "/form";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/edit.do")
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		return urlContext + "/form";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/view.do")
	public String view(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		return urlContext + "/view";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") T entity,
			BindingResult bindingResult, ModelMap model) {
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
		beforeSave(request, entity, model);
		// merge into datasource
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/bulkdelete.do")
	public String bulkdelete(@RequestParam("id") String[] id) {
		if (id != null) {
			for (int i = 0; i < id.length; i++) {
				try {
					genericDAO.deleteById(getEntityClass(),
							Long.parseLong(id[i]));
				} catch (Exception ex) {
					ex.printStackTrace();
					log.warn("Error deleting record " + id, ex);
				}
			}
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/delete.do")
	public String delete(@ModelAttribute("modelObject") T entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
			request.getSession().setAttribute("error","Cannot delete a parent row");
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}

	@ModelAttribute("modelObject")
	public T setupModel(HttpServletRequest request) {
		String pk = request.getParameter(getPkParam());
		if (pk == null || org.apache.commons.lang.StringUtils.isEmpty(pk)) {
			return getEntityInstance();
		} else {
			return genericDAO.getById(getEntityClass(), Long.parseLong(pk));
		}
	}

	protected void beforeSave(HttpServletRequest request,
			@ModelAttribute("modelObject") T entity, ModelMap model) {
		if (entity instanceof AbstractBaseModel) {
			if (((AbstractBaseModel) entity).getId() == null) {
				((AbstractBaseModel) entity).setCreatedAt(Calendar
						.getInstance().getTime());
				if (((AbstractBaseModel) entity).getCreatedBy()==null) {
					((AbstractBaseModel) entity).setCreatedBy(getUser(request)
							.getId());
				}
			} else {
				((AbstractBaseModel) entity).setModifiedAt(Calendar
						.getInstance().getTime());
				if (((AbstractBaseModel) entity).getModifiedBy()==null) {
					((AbstractBaseModel) entity).setModifiedBy(getUser(request)
							.getId());
				}
			}
		}
	}

	public void setupCreate(ModelMap model, HttpServletRequest request) {
		// Default is no implementation
	}

	public void setupUpdate(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
	}

	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		List<StaticData> statuses = listStaticData("ENTITY_STATUS");
		model.addAttribute("statuses", statuses);
	}

	public void cleanUp(HttpServletRequest request) {

	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		List columnPropertyList = (List) request.getSession().getAttribute(
				"columnPropertyList");
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");

		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html"))
			response.setHeader("Content-Disposition", "attachment;filename="
					+ urlContext + "Report." + type);
		try {
			criteria.setPageSize(100000);
			String label = getCriteriaAsString(criteria);
			ByteArrayOutputStream out = dynamicReportService.exportReport(
					urlContext + "Report", type, getEntityClass(),
					columnPropertyList, criteria, request);
			out.writeTo(response.getOutputStream());
			if (type.equals("html"))
				response.getOutputStream()
						.println(
								"<script language=\"javascript\">window.print()</script>");
			criteria.setPageSize(25);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		}
	}

	// Set up any custom editors, adds a custom one for java.sql.date by default

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}
	
	protected void setModifier(HttpServletRequest request, AbstractBaseModel entity) {
		if (entity.getId() == null) {
			entity.setCreatedAt(Calendar.getInstance().getTime());
			if (entity.getCreatedBy() == null) {
				entity.setCreatedBy(getUserId(request));
			}
		} else {
			entity.setModifiedAt(Calendar.getInstance().getTime());
			if (entity.getModifiedBy() == null) {
				entity.setModifiedBy(getUserId(request));
			}
		}
	}
	
	protected Long getUserId(HttpServletRequest request) {
		User user = getUser(request);
		return user.getId();
	}

}
