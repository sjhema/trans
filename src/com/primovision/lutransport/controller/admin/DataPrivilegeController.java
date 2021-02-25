package com.primovision.lutransport.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.core.util.CoreUtil;

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.model.BusinessObject;
import com.primovision.lutransport.model.DataPrivilege;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.hr.EmployeeCatagory;

@Controller
@RequestMapping("/admin/access/dataprivilege")
public class DataPrivilegeController extends CRUDController<DataPrivilege> {
	public DataPrivilegeController() {
		setUrlContext("admin/access/dataprivilege");
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		super.setupCreate(model, request);
		
		String query = "select obj from EmployeeCatagory obj where obj.status=1 order by obj.name";
		List<EmployeeCatagory> empCategories = genericDAO.executeSimpleQuery(query);
		List<String> empCatNames = new ArrayList<String>();
		for (EmployeeCatagory anEmpCat : empCategories) {
			empCatNames.add(anEmpCat.getName());
		}
		model.addAttribute("empCatNames", empCatNames);
		
		String roleId = request.getParameter("roleId");
		Role role = genericDAO.getById(Role.class, Long.valueOf(roleId));
		
		DataPrivilege entity = (DataPrivilege)model.get("modelObject");
		entity.setRole(role);
		
		query = "select obj from DataPrivilege obj where obj.status=1"
				+ " and role=" + roleId
				+ " and dataType=" + "'" + DataPrivilege.DATA_TYPE_EMP_CAT + "'";
		List<DataPrivilege> dataPrivilegeList = genericDAO.executeSimpleQuery(query);
		if (dataPrivilegeList == null || dataPrivilegeList.isEmpty()) {
			entity.setPrivilegeArrPayrollReport(new String[0]);
			entity.setPrivilegeArrManageEmployee(new String[0]);
			return;
		}
		
		String empCatPrivilege = StringUtils.EMPTY;
		List<String> accessibleEmpCats = new ArrayList<String>();
		String[] accessibleEmpCatsArr = null;
		for (DataPrivilege aDataPrivilege : dataPrivilegeList) {
			accessibleEmpCats.clear();
			empCatPrivilege = aDataPrivilege.getPrivilege();
			if (StringUtils.isEmpty(empCatPrivilege)) {
				continue;
			}
			
			String[] empCatIdArr = empCatPrivilege.split(",");
			for (String empCatId : empCatIdArr) {
				accessibleEmpCats.add(retrieveEmpCatName(empCategories, empCatId));
			}
			accessibleEmpCatsArr = accessibleEmpCats.toArray(new String[0]);
			
			long boId = aDataPrivilege.getBo().getId().longValue();
			if (boId == payrollReportBOId) {
				entity.setPrivilegeArrPayrollReport(accessibleEmpCatsArr);
			} else if (boId == manageEmployeeBOId) {
				entity.setPrivilegeArrManageEmployee(accessibleEmpCatsArr);
			}
		}
	}
	
	private String retrieveEmpCatName(List<EmployeeCatagory> empCategories, String id) {
		String empCatName = StringUtils.EMPTY;
		Long empCatId = Long.valueOf(id);
		for (EmployeeCatagory anEmpCat : empCategories) {
			if (anEmpCat.getId().longValue() == empCatId.longValue()) {
				empCatName = anEmpCat.getName();
			}
		}
		return empCatName;
	}
	
	private String buildEmpCatPrivilege(String[] privilegeArr) {
		String privilege = StringUtils.EMPTY;
		if (privilegeArr == null || privilegeArr.length <= 0) {
			return privilege;
		}
		
		for (String aPrivilege : privilegeArr) {
			EmployeeCatagory empCat = retrieveEmpCat(aPrivilege);
			privilege += (empCat.getId() + ",");
		}
		return privilege.substring(0, privilege.length() - 1);
	}
	
	private EmployeeCatagory retrieveEmpCat(String name) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("name", name);
		List<EmployeeCatagory> empCatList = genericDAO.findByCriteria(EmployeeCatagory.class, criterias, "name", false);
		return empCatList.get(0);
	}
	
	@Override
	public String save(HttpServletRequest request, DataPrivilege entity,
			BindingResult bindingResult, ModelMap model) {
		String query = "select obj from DataPrivilege obj where obj.status=1"
				+ " and role=" + entity.getRole().getId()
				+ " and dataType=" + "'" + DataPrivilege.DATA_TYPE_EMP_CAT + "'";
		List<DataPrivilege> dataPrivilegeList = genericDAO.executeSimpleQuery(query);
		for (DataPrivilege aDataPrivilege : dataPrivilegeList) {
			genericDAO.delete(aDataPrivilege);
		}
		
		DataPrivilege entityToBeSaved = new DataPrivilege();
		entityToBeSaved.setDataType(DataPrivilege.DATA_TYPE_EMP_CAT);
		entityToBeSaved.setRole(entity.getRole());
		BusinessObject bo = genericDAO.getById(BusinessObject.class, Long.valueOf(payrollReportBOId));
		entityToBeSaved.setBo(bo);
		String privilege = buildEmpCatPrivilege(entity.getPrivilegeArrPayrollReport());
		entityToBeSaved.setPrivilege(privilege);
		setModifier(request, entityToBeSaved);
		genericDAO.save(entityToBeSaved);
		
		entityToBeSaved = new DataPrivilege();
		entityToBeSaved.setDataType(DataPrivilege.DATA_TYPE_EMP_CAT);
		entityToBeSaved.setRole(entity.getRole());
		bo = genericDAO.getById(BusinessObject.class, Long.valueOf(manageEmployeeBOId));
		entityToBeSaved.setBo(bo);
		privilege = buildEmpCatPrivilege(entity.getPrivilegeArrManageEmployee());
		entityToBeSaved.setPrivilege(privilege);
		setModifier(request, entityToBeSaved);
		genericDAO.save(entityToBeSaved);
		
		return "redirect:/admin/access/role/list.do";
	}
}
