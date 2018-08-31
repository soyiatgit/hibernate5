package com.saurabh.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saurabh.models.Address;

@Entity
// if you don't provide Table name, by default the class name becomes table name
// but we do not want that strong coupling, so we specify this by giving a name.
@Table(name = "employee_table")
// with the below annotation hibernate just sets only the changed attribute while updating
@DynamicUpdate
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="employee_id")
	private Integer id;
	
	@Column(name="employee_name", length=36, nullable=false)
	private String name;
	
	@Column(name="employee_email")
	private String email;
	
	@Column(name="employee_doj")
	private Date doj;
	
	@Column(name="employee_salary")
	private Double salary;
	
	@Column(name="employee_gender")
	private String gender;
	
	/**
	 * Using the below Annotation will insert this collection in to a new table.
	 * The new table will be joined by the primary key of this entity.
	 * 
	 * The new table however won't have a primary key and hence this way of inserting
	 * collections in to an entity is not efficient. Rather use One to Many association.
	 * 
	 * The name of the new table by default will be NameOfThisEntity_NameOfVariableBelow
	 * This however can be customized using following @JoinTable annotation.
	 * 
	 * Though the default JPA implementation doesn't provide a primary key to such table,
	 * We can use the Hibernate-specific Sequence Generator to assign a primary key as
	 * shown below using @GenericGenerator and @CollectionId annotation.
	 * 
	 * Had to replace - with _. Probably postgre doesn't support  '-' in sequence generator.
	 * 
	 * While retrieving any entity Hibernate by default doesn't load the collections in it. If 
	 * you need to load collections too use the below annotation as: @ElementCollection(fetch=FetchType.EAGER)
	 */
	@ElementCollection
	@JoinTable(name="address_table", joinColumns=@JoinColumn(name="employee_id"))
	@GenericGenerator(name = "address_id_gen", strategy = "sequence")
	@CollectionId(columns = { @Column (name="address_id")}, generator = "address_id_gen", type = @Type(type = "int"))
	private List<Address> addressList = new ArrayList<>();
	
	/**
	 * the below annotation describes the one to one mapping in hibernate.
	 * This is to say that an employee can only have a single family detail.
	 * 
	 * @OneToOne mapping is one of the types of association in Hibernate.
	 * Association is only formed with Entities. So the Family (unlike the address)
	 * has to be a regular Hibernate entity with its own primary key.
	 * 
	 * Hibernate will create a table for Family using its own entity definition.
	 * The Employee table will have a new column for the familyDetails. This column
	 * name by default will be familyDetails_family_id (NameOfObjBelow_PKOfBelowEntity).
	 * 
	 * However if required the name of the column can be changed using @JoinColumn
	 * 
	 * CascadeType.ALL suggests that the familyDetails obj will be saved while
	 * saving the Employee entity. Otherwise we explicitly have to save Family object
	 * after saving the employee object.
	 */
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="family_id")
	private Family familyDetails;
	
	
	/**
	 * We have already linked multiple addresses to the Employee entity using Embedded Collections
	 * This however is not a good approach for the reasons stated above.
	 * 
	 * The same can be done using the OneToMany association in Hibernate.
	 * This way Hibernate creates a new table for the mapped entity using its definition
	 * and make a foreign key constraints with the Parent Entity.
	 * 
	 * The problem in this case however is there is no way to look at both tables and tell
	 * the relation because there is no mapping column in either tables. Both tables have column
	 * respective to their entity description.
	 * 
	 * Hibernate creates a mapping table or joining table where the PK of both above entities are
	 * mapped together so that its possible to look at that table and establish the relation.
	 * 
	 * Original OneToMany Mapping
	 *	
	 * @OneToMany(cascade=CascadeType.ALL)
	 * private List<Account> accounts;
	 *
	 * But such a table has no other use and it has just two column and no PK as well.
	 * We can get rid of this table, if instead of @OneToMany from the parent entity 
	 * we establish a @ManyToOne (technically they are same) relation from the 
	 * child entity and set the reference of Parent in to the child entity while saving.
	 * Also create a bi-directional relation in the parent class. The relation is now 
	 * maintained by mappedBy attribute below, which is mandatory in this case.
	 * 
	 * The @JsonManagedReference is required to resolve the issue of conflicting JSON
	 * problem arising due to presence of Account list in Employee and employee in
	 * Account entity
	 */
	@JsonManagedReference
	@OneToMany(cascade=CascadeType.ALL, mappedBy="employee")
	private List<Account> accounts = new ArrayList<Account>();
	
	/**
	 * An employee can work in multiple projects and Each project has more than one 
	 * employee. Such a mapping is Many to Many mapping. Since in this case both side 
	 * has many entities, Hibernate must create a joining table to maintain the 
	 * relationship.
	 * 
	 * By default Hibernate creates two joining table, one for each entity involved.
	 * These tables are obviously identical in data nature. This however can be 
	 * prevented by assigining a mappedBy ownership on either entity. This way we tell Hibernate
	 * that the other side is already taking care of maintaining relationship and no join
	 * table is required froom this end.
	 */
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="employee_address_table",
	joinColumns=@JoinColumn(name="employee_id"),
	inverseJoinColumns=@JoinColumn(name="project_id"))
	private List<Project> projects;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDoj() {
		return doj;
	}

	public void setDoj(Date doj) {
		this.doj = doj;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}
 
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setFamilyDetails(Family familyDetails) {
		this.familyDetails = familyDetails;
	}
	
	public Family getFamilyDetails() {
		return familyDetails;
	}
	
	public void setAccounts(List<Account> accounts) {
		if(null != this.accounts && this.accounts.size() > 0) {
			this.accounts.clear();
		}
		for(Account account: accounts) {
			account.setEmployee(this);
			this.accounts.add(account);
		}
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public List<Project> getProjects() {
		return projects;
	}
}
