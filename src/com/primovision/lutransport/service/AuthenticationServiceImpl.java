package com.primovision.lutransport.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.User;

public class AuthenticationServiceImpl implements AuthenticationService {
	protected static Logger log = Logger.getLogger(AuthenticationService.class
			.getName());

	@Autowired
	private GenericDAO genericDAO;

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	@Override
	public List<Role> findRolesForURL(String url) {
		StringBuffer query = new StringBuffer("");
		query.append("select r from Role r, RolePrivilege rp where r.id=rp.role.id and rp.businessObject.url like '%/"
				+ url + "%'");
		return genericDAO.executeSimpleQuery(query.toString());
	}

	@Override
	public boolean hasUserPermission(User user, String url) {
		StringBuffer query = new StringBuffer("");
		if (url != null && url.indexOf("?") != -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		log.debug("URL is : " + url);
		query.append("select u from User u, Role r, RolePrivilege rp where u.id="
				+ user.getId()
				+ " and u.role.id = r.id and r.id=rp.role.id and rp.businessObject.url like '%"
				+ url + "%'");
		List<User> users = genericDAO.executeSimpleQuery(query.toString());
		if (users != null && users.size() > 0)
			return true;
		return false;
	}
	
	@Override
	public boolean hasUserPermissionByBOName(User user, String boName) {
		StringBuffer query = new StringBuffer("select u from User u, Role r, RolePrivilege rp where ");
		query.append("u.id=" + user.getId())
				.append(" and u.role.id = r.id and r.id=rp.role.id")
				.append(" and rp.businessObject.objectName = '" + boName + "'");
		List<User> users = genericDAO.executeSimpleQuery(query.toString());
		return (users != null && !users.isEmpty());
	}
}
