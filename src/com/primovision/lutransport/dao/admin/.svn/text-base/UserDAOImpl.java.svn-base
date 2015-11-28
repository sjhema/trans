package com.primovision.lutransport.dao.admin;

import java.util.HashMap;
import java.util.Map;

import com.primovision.lutransport.core.dao.GenericJpaDAO;
import com.primovision.lutransport.model.User;

@SuppressWarnings("unchecked")
public class UserDAOImpl extends GenericJpaDAO implements UserDAO {

    @Override
    public User getUserByName(String name) {
	Map map = new HashMap();
	map.put("name", name);
	return getByNamedQuery("user.getByName", map);
    }
}
