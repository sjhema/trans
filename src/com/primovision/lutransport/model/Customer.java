package com.primovision.lutransport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="customer")
public class Customer extends AbstractBaseModel {
	
	@NotEmpty
	/*private String name;*/
	//@Size(max=40)
	@Column(name="name")
	private String name;
	
	@Column(name="customer_name_id")
	private String customerNameID;
	
	//@NotEmpty
	@Column(name="address")
	private String address;
	
	@Column(name="address2")
	private String address2;
	
	//@NotEmpty
	@Column(name="city")
	private String city;
	
    @ManyToOne
	@JoinColumn(name="state")
	 private State state;
	
	//@NotNull 
	@Column(name="zipcode")
	private String zipcode;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="fax")
	private String fax;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getZipcode() {
		return zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public void setCustomerNameID(String customerNameID) {
		this.customerNameID = customerNameID;
	}
	
	public String getCustomerNameID() {
		return customerNameID;
	}
	
	
}
