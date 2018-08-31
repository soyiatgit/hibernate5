package com.saurabh.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="account_table")
public class Account {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="account_id")
	private Integer id;
	
	@Column(name="bank_name")
	private String bankName;
	
	@Column(name="bank_code")
	private String bankCode;
	
	@Column(name="account_type")
	private String accountType;
	
	@ManyToOne
	@JoinColumn(name="employee_id")
	@JsonBackReference
	/**
	 * The @JsonManagedReference is required to resolve the issue of conflicting JSON
	 * problem arising due to presence of Account list in Employee and employee in
	 * Account entity
	 */
	private Employee employee;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public Employee getEmployee() {
		return employee;
	}
}
