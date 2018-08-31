package com.saurabh.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "family_table")
public class Family {

	@Id
	@Column(name = "family_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "father_name")
	private String fatherName;

	@Column(name = "mother_name")
	private String motherName;

	@Column(name = "spouse_name")
	private String spouseName;
	
	/**
	 * Since the Employee entity is the parent entity that holds the family obj, We can 
	 * fetch the family details by fetching the Employee entity.
	 * 
	 * However, if we have the familyId, how can we fetch the Employee details.
	 * This can be done using a bi-directional mapping as shown below.
	 * 
	 * We keep an employee reference in the family entity, but not as a column
	 * because that will again make a foreign key of employee in Family table which 
	 * is not possible.
	 * 
	 * We just keep the reference as a one to one binding (since the relation is one to one
	 * from family entity perspective as well) and tell hibernate how this
	 * field is mapped with the parent table. This tells hibernate the Employee Entity already
	 * takes care of the relationship and this entity just has to retrieve the data.
	 * 
	 * This makes it possible to fetch the employee data as well by querying Family table.
	 * 
	 * Note: No extra column is created in this process in either table.
	 */
	@OneToOne(mappedBy="familyDetails")
	private Employee employee;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFatherName() {
		return fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

}
