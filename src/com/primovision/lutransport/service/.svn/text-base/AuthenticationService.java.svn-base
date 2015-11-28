package com.primovision.lutransport.service;

import java.util.List;

import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.User;

public interface AuthenticationService {
    List<Role> findRolesForURL(String url);

    public boolean hasUserPermission(User user, String url);
}
