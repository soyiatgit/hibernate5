package com.saurabh.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name="animal_table")
@Inheritance(strategy=InheritanceType.JOINED)
// below annotation defines the value for name and type of the discriminator column
// which defaults to DTYPE otherwise. Read more in Animal Service.
// @DiscriminatorColumn(name="animal_type", discriminatorType=DiscriminatorType.STRING)
// Discriminator value corresponding to the entry for this class
// @DiscriminatorValue(value="ANIMAL")
public class Animal {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="animal_id")
	private Integer id;
	
	@Column(name="animal_name")
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
