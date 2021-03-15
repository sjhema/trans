package com.primovision.lutransport.service;

import java.util.List;

import com.primovision.lutransport.model.DataPrivilege;
import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.User;

public interface AuthenticationService {
    List<Role> findRolesForURL(String url);

    public boolean hasUserPermission(User user, String url);
    public boolean hasUserPermissionByBOName(User user, String boName);
    
    public DataPrivilege retrieveDataPrivilege(User user, String url);
}
