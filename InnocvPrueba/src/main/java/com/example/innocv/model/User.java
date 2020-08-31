package com.example.innocv.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@NotEmpty
	@Size(min = 3,max = 15)
	@Column(nullable = false)
	private String name;

	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	public User() {}

	public User(String id, String name, Date birthdate) {
		this.id = id;
		this.name = name;
		this.birthdate=birthdate;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id=id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String toString() {
		return "Id: " + this.id+ " Name: "+this.name+ " birthdate: "+ this.birthdate;
	}
}
