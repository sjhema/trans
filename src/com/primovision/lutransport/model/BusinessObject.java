package com.primovision.lutransport.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * 
 */
@Entity
@Table(name = "business_object")
@SuppressWarnings("serial")
public class BusinessObject extends AbstractBaseModel implements Comparable<BusinessObject>{	

	@Override
	public int compareTo(BusinessObject obj) {
		if(obj==null)
			return 0;
		if(obj.getDisplayOrder()==null || this.getDisplayOrder()==null)
			return 0;
		return this.displayOrder-obj.displayOrder;
	}
	
	@Column(name="url_context")
	private String urlContext;
	/**
	 * Attribute objectName.
	 */
	@Column(name = "object_name", length = 60)
	private String objectName;

	@Column(name="action")
	private String action;
	/**
	 * Attribute url.
	 */
	@Column(name = "url", length = 500)
	private String url;
	
	@Column(name="hierarchy")
	private String objectHierarchy;

	/**
	 * Attribute objectParent.
	 */
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private BusinessObject parent;

	/**
	 * Attribute objectLevel.
	 */
	@Column(name="object_level")
	private Integer objectLevel;
	
	
	@Column(name="hidden")
	private Integer hidden;

	/**
	 * List of RolePrivilege
	 */
	@OneToMany(mappedBy = "businessObject", fetch=FetchType.LAZY)
	private List<RolePrivilege> rolePrivileges = new ArrayList<RolePrivilege>(0);
	
	@OneToMany(mappedBy = "parent")
	private List<BusinessObject> children = new ArrayList<BusinessObject>(0);
	
	/**
	 * Attribute dispayOrder.
	 */
	@Column(name="display_order")
	private Integer displayOrder;
	/**
	 * <p>
	 * </p>
	 * 
	 * @return objectName
	 */
	@Basic
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName
	 *            new value for objectName
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}


	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            new value for url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @return objectLevel
	 */
	public Integer getObjectLevel() {
		return objectLevel;
	}

	/**
	 * @param objectLevel
	 *            new value for objectLevel
	 */
	public void setObjectLevel(Integer objectLevel) {
		this.objectLevel = objectLevel;
	}
	

	/**
	 * Get the list of RolePrivilege
	 */
	public List<RolePrivilege> getRolePrivileges() {
		return this.rolePrivileges;
	}

	/**
	 * Set the list of RolePrivilege
	 */
	public void setRolePrivileges(List<RolePrivilege> rolePrivileges) {
		this.rolePrivileges = rolePrivileges;
	}

	/**
	 * @return the parent
	 */
	public BusinessObject getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(BusinessObject parent) {
		this.parent = parent;
	}
	
	/**
	 * @return the children
	 */
	public List<BusinessObject> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<BusinessObject> children) {
		this.children = children;
	}
	
	public void setObjectHierarchy(String objectHierarchy) {
		StringBuffer buffer = new StringBuffer("/"+this.id);
		while(getParent()!=null) {
			buffer.append(getParent().getObjectHierarchy());
		}
		this.objectHierarchy = buffer.toString();
	}

	public String getObjectHierarchy() {
		return objectHierarchy;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getHidden() {
		return hidden;
	}

	public void setHidden(Integer hidden) {
		this.hidden = hidden;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUrlContext() {
		return urlContext;
	}

	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
	}
}