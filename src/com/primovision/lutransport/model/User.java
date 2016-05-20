package com.primovision.lutransport.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "user_info")
@NamedQueries({@NamedQuery(name = "user.getByName", query = "select obj from User obj where obj.username=:name")})
public class User extends AbstractBaseModel implements Comparable, Auditable{
	
	private static final long serialVersionUID = 1807241954265797561L;
	
	@Column(name = "username")
	@NotEmpty(message = "User name is required.")
	private String username;
	
	@Column(name = "password")
	@NotEmpty(message = "Password is required.")
	@Size(min = 5, message = "Password should be of minimum 5 characters")
	@Pattern(regexp = "[a-zA-Z]*[0-9]*", message = "Password should be alpanumeric.")
	private String password;
	
	@Transient
	private String confirmPassword;
	
	@Column(name = "email")
	@Email(message = "Invalid email")
	private String email;
	
	@Column(name = "first_name")
	@NotEmpty(message = "First Name is required.")
	private String firstName;
	
	@NotEmpty(message = "Last Name is required.")
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "phone_number")
	@Pattern(regexp = "[0-9]*", message = "Phone number should be numeric.")
	private String phoneNumber;
	
	@Pattern(regexp = "[0-9]*", message = "Mobile number should be numeric.")
	@Column(name = "mobile_no")
	private String mobileNo;
	
	@Column(name = "last_login_date")
	private Date lastLoginDate;
	
	@Column(name = "bill_batch_date")
	private Date billBatchDate;
	
	@Column(name = "login_attempts")
	private Integer loginAttempts = 0;
	
	@Column(name = "account_status")
	private Integer accountStatus = 0;
	
	@Column(name = "user_type")
	private Integer userType;
	
	@Transient
	private String membershipType;
	
	@Column(name = "agree_terms")
	private Byte agreeTerms = 0;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	
	@Transient
	public String getFullName() {
		String fullName = StringUtils.EMPTY;
		if (!StringUtils.isEmpty(getLastName())) {
			fullName = getLastName();
		}
		if (!StringUtils.isEmpty(getFirstName())) {
			fullName += " " + getFirstName();
		}
			
		return fullName;	
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * @return the mobileNo
	 */
	public String getMobileNo() {
		return mobileNo;
	}
	
	/**
	 * @param mobileNo
	 *            the mobileNo to set
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	public Integer getLoginAttempts() {
		return loginAttempts;
	}
	
	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
	
	public Integer getAccountStatus() {
		return accountStatus;
	}
	
	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public Integer getUserType() {
		return userType;
	}
	
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	@Transient
	public boolean isOmniAdmin() {
		if ("ADMIN".equalsIgnoreCase(role.getName())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Object otherUser) {
		if (otherUser != null)
			return this.username.compareToIgnoreCase(((User) otherUser)
					.getUsername());
		return 0;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public List getAuditableFields() {
		List props = new ArrayList();
		props.add("username");
		props.add("email");
		props.add("firstName");
		props.add("lastName");
		props.add("mobileNo");
		props.add("lastLoginDate");
		props.add("accountStatus");
		props.add("status");
		return props;
	}
	
	public Byte getAgreeTerms() {
		return agreeTerms;
	}
	
	public void setAgreeTerms(Byte agreeTerms) {
		this.agreeTerms = agreeTerms;
	}
	
	@Override
	public String getAuditMessage() {
		return null;
	}
	
	public String getMembershipType() {
		return membershipType;
	}
	
	public void setMembershipType(String membershipType) {
		this.membershipType = membershipType;
	}

	@Override
	public String getPrimaryField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean skipAudit() {
		// TODO Auto-generated method stub
		return false;
	}

	public Date getBillBatchDate() {
		return billBatchDate;
	}

	public void setBillBatchDate(Date billBatchDate) {
		this.billBatchDate = billBatchDate;
	}
}
