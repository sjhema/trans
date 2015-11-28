package com.primovision.lutransport.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="user_role")
@NamedQueries({@NamedQuery(name = "selectRoleById", query = "select role from Role role where role.id NOT IN(SELECT ur.role.id FROM UserRole ur WHERE ur.user.id=:id)") ,
@NamedQuery(name = "selectUserRoleById", query = "select obj from UserRole obj where obj.role.id!=:id and obj.user.id=:userid")})

public class UserRole extends AbstractBaseModel {
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
