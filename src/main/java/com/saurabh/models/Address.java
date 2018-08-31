package com.saurabh.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/** the below annotation is used to create an embedded value object.
 * This means the following class is not an entity rather a value object
 * Such classes are embbeded in to the parent entity where they are used.
 * 
 * The embedded object in parent entity should be annotated with 
 * @Embedded annotation.
 * 
 * This is one way of maintaining collection inside an entity rather than
 * using One to Many association.
 * 
 * This way although is easy but is not efficient because such embedded 
 * collections do not have an Id element and hence Hibernate can't manage 
 * them efficently. Every time an update is made the entire collection
 * against the entity is dropped and all entries are inserted again.
 * 
 * This should only be used if the collection size is very small so that
 * Hibernate do not have to perform many SQL opertions.
 * @author saurabhtiwari
 *
 */

@Embeddable
public class Address {
	
	@Column(name="city")
	private String city;
	
	@Column(name="pincode")
	private Long pincode;
	
	@Column(name="state")
	private String state;
	
	@Column(name="street")
	private String street;

	public String getCity() {
		return city;
	}

	public Long getPincode() {
		return pincode;
	}

	public String getState() {
		return state;
	}

	public String getStreet() {
		return street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStreet(String street) {
		this.street = street;
	}

}
