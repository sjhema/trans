package com.primovision.lutransport.dao.admin;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.User;

public interface UserDAO extends GenericDAO {

    User getUserByName(String name);
}
