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

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.model.BusinessObject;
import com.primovision.lutransport.model.DataPrivilege;
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
		
		List<DataPrivilege> dataPrivilegeList = retrieveDataPrivileges(role.getId());
		if (dataPrivilegeList == null || dataPrivilegeList.isEmpty()) {
			entity.setPrivilegeArrEmpCatPayrollReport(new String[0]);
			entity.setPrivilegeArrEmpCatManageEmployee(new String[0]);
			return;
		}
		
		for (DataPrivilege aDataPrivilege : dataPrivilegeList) {
			String privilege = aDataPrivilege.getPrivilege();
			if (StringUtils.isEmpty(privilege)) {
				continue;
			}
			
			if (StringUtils.equals(DataPrivilege.DATA_TYPE_EMP_CAT, aDataPrivilege.getDataType())) {
				populateEmpCatPrivilege(entity, aDataPrivilege, empCategories);
			}
		}
	}
	
	private void populateEmpCatPrivilege(DataPrivilege entity, DataPrivilege aDataPrivilege, List<EmployeeCatagory> empCategories) {
		String empCatPrivilege = aDataPrivilege.getPrivilege();
		if (StringUtils.isEmpty(empCatPrivilege)) {
			return;
		}
		
		List<String> accessibleEmpCats = new ArrayList<String>();
		String[] empCatIdArr = empCatPrivilege.split(",");
		for (String empCatId : empCatIdArr) {
			accessibleEmpCats.add(retrieveEmpCatName(empCategories, empCatId));
		}
		String[] accessibleEmpCatsArr = accessibleEmpCats.toArray(new String[0]);
			
		long boId = aDataPrivilege.getBo().getId().longValue();
		if (boId == payrollReportBOId) {
			entity.setPrivilegeArrEmpCatPayrollReport(accessibleEmpCatsArr);
		} else if (boId == manageEmployeeBOId) {
			entity.setPrivilegeArrEmpCatManageEmployee(accessibleEmpCatsArr);
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
	
	private List<DataPrivilege> retrieveDataPrivileges(Long roleId) {
		String query = "select obj from DataPrivilege obj where obj.status=1"
				+ " and role=" + roleId;
				//+ " and dataType=" + "'" + DataPrivilege.DATA_TYPE_EMP_CAT + "'";
		List<DataPrivilege> dataPrivilegeList = genericDAO.executeSimpleQuery(query);
		return dataPrivilegeList;
	}
	
	private void deleteDataPrivileges(Long roleId) {
		List<DataPrivilege> dataPrivilegeList = retrieveDataPrivileges(roleId);
		for (DataPrivilege aDataPrivilege : dataPrivilegeList) {
			genericDAO.delete(aDataPrivilege);
		}
	}
	@Override
	public String save(HttpServletRequest request, DataPrivilege entity,
			BindingResult bindingResult, ModelMap model) {
		Role role = entity.getRole();
		deleteDataPrivileges(role.getId());
		
		saveEmpCatDataPrivileges(request, role, entity);
		
		return "redirect:/admin/access/role/list.do";
	}
	
	private void saveEmpCatDataPrivileges(HttpServletRequest request, Role role, DataPrivilege entity) {
		String empCatDataType = DataPrivilege.DATA_TYPE_EMP_CAT;
		String privilege = buildEmpCatPrivilege(entity.getPrivilegeArrEmpCatPayrollReport());
		save(request, role, payrollReportBOId, empCatDataType, privilege);
		privilege = buildEmpCatPrivilege(entity.getPrivilegeArrEmpCatManageEmployee());
		save(request, role, manageEmployeeBOId, empCatDataType, privilege);
	}
	
	private void save(HttpServletRequest request, Role role, long boId, String dataType, String privilege) {
		if (StringUtils.isEmpty(privilege)) {
			return;
		}
		
		DataPrivilege entity = new DataPrivilege();
		entity.setDataType(dataType);
		entity.setRole(role);
		BusinessObject bo = genericDAO.getById(BusinessObject.class, Long.valueOf(boId));
		entity.setBo(bo);
		entity.setPrivilege(privilege);
		setModifier(request, entity);
		genericDAO.save(entity);
	}
}
