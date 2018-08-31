package com.saurabh.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.saurabh.entities.Account;
import com.saurabh.entities.Employee;
import com.saurabh.entities.Family;
import com.saurabh.entities.Project;
import com.saurabh.models.Address;
import com.saurabh.utils.HibernateUtil;

@Service
public class EmployeeService {
	
	public void saveEmployee() {
		Employee employee = getEmployeeObj();
		try(Session session = HibernateUtil.getSessionFactory().openSession();) {
			session.beginTransaction();
			// persist and saveOrupdate() return void while save returns the generated PK.
			session.save(employee);
			// also important to note is that the .merge() method can save an object which was retrieved in
			// a separate session than where it is being saved. Method .update(), on the other hand can save
			// the state of the object retrieved in the same session. If you wish to save object regardless
			// of their states then use .merge().
			session.getTransaction().commit();
		} catch(Exception ex) {
			System.out.println("Error occured while saving the employee :: " + ex.getMessage());
		}
	}

	public Employee getEmployee(Integer id) {
		System.out.println("Inside the service : getEmployee()");
		Employee employee = null;
		List<Address> employeeAddresses;
		Family family;
		try(Session session = HibernateUtil.getSessionFactory().openSession();) {
			session.beginTransaction();
			// prior to Hibernate 5, the below code returned an Object type that had to
			// be type casted, but not so any more.
			employee = session.get(Employee.class, id);
			
			// if an entity is not loaded eagerly (by default they are not), the collections
			// within the entity are not loaded. However, Hibernate uses a proxy to load such entities
			// and if we specifically call the getter for collections, Hibernate loads them then.
			// for Ex: We do not have the address collection yet in the above employee object because
			// the address is marked for Lazy load (by Default). But as soon as I call getAddressList()
			// Hibernate will fetch the address list dynamically.
			// This call should be made within this try block where the session is available. Once the 
			// session is closed this dynamic loading will not work and we will get an exception.
			
			// works here
			employeeAddresses = employee.getAddressList();
			
			/**
			 * this is to be noted that the One to One mapping is always eagerly loaded by default.
			 * So the employee object above by default has the family object.
			 */
			family = employee.getFamilyDetails();
			
		} catch(Exception ex) {
			System.out.println("Error occured while fetching the employee :: " + ex.getMessage());
		}
		
		// won't work here
		// employeeAddresses = employee.getAddressList();

		return employee;
	}
	
	public List<Employee> getEmployees() {
		System.out.println("Inside the service : getEmployee()");
		List<Employee> employees = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession();) {
			session.beginTransaction();
			// prior to Hibernate 5, the below code returned an Object type that had to
			// be type casted, but not so any more.
			employees = session.createCriteria(Employee.class).list();
			// persist and saveOrupdate() return void while save returns the generated PK.
			// session.save(employee);
		} catch(Exception ex) {
			System.out.println("Error occured while fetching the employees :: " + ex.getMessage());
		}

		return employees;
	}
	
	private Employee getEmployeeObj() {
		Employee employee = new Employee();
		employee.setName("Saurabh Tiwari");
		employee.setEmail("saurabh.tiwari@gmail.com");
		employee.setSalary(200000.00);
		employee.setDoj(new Date());
		employee.setGender("Male");
		employee.setFamilyDetails(getFamilyObj());
		employee.setAddressList(getAddressList());
		employee.setProjects(getProjectList());
		employee.setAccounts(getAccountList());
		
		List<Project> projectList = getProjectList();
		employee.setProjects(projectList);
		
		/**
		 * Since we have specified a @ManyToOne relation from accounts to Employee
		 * we also need to set the correct EmployeeList reference in each project object.
		 * 
		 * In this case we have employee object which have 2 projects in it.
		 * and other employee1 object which have no projects in it.
		 * 
		 * However both the employees are assigned to each project such that each project 
		 * has two employees (1 and 2), while only one employee (1) has two project. If 
		 * needed employee(2) can contains projects too say projects (3 and 4).
		 * 
		 * So we are trying to save employee1 which contains multiple projects and these
		 * project in turn contain an additional employee apart from employee1
		 */
		Employee employee1 = new Employee();
		employee1.setName("Rajan Kadwal");
		employee1.setEmail("rajan.kadwal@gmail.com");
		employee1.setSalary(900000.00);
		employee1.setDoj(new Date());
		employee1.setGender("Male");
		employee1.setFamilyDetails(getFamilyObj());
		employee1.setAddressList(getAddressList());
		employee1.setAccounts(getAccountList());
		
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(employee);
		employees.add(employee1);
		projectList.forEach((project)-> project.setEmployees(employees));
		
		// Since we have specified a @ManyToOne relation from accounts to Employee
		// we also need to set the correct Employee reference in each account object.
		// Currently this is done in Employee Entity constructor method. However you 
		// can do it as shown below too.
		// accountList.forEach((account)-> account.setEmployee(employee));
		return employee;
	}
	
	private Family getFamilyObj() {
		Family family = new Family();
		family.setFatherName("N.B. Tiwari");
		family.setMotherName("Geeta Tiwari");
		family.setSpouseName("YTD");
		return family;
	}
	
	private List<Address> getAddressList() {
		List<Address> addressList = new ArrayList<>();
		Address address = new Address();
		address.setCity("Lalkuan");
		address.setPincode(262402L);
		address.setState("Uttarakhand");
		address.setStreet("Daulia D Class");
		
		Address address1 = new Address();
		address1.setCity("Gurgaon");
		address1.setPincode(122001L);
		address1.setState("Haryana");
		address1.setStreet("Sector-14");
		
		addressList.add(address);
		addressList.add(address1);
		return addressList;
	}
	
	private List<Account> getAccountList() {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountType("Savings");
		account.setBankCode("SBIN0002567");
		account.setBankName("State Bank Of India");
		
		Account account1 = new Account();
		account1.setAccountType("Savings");
		account1.setBankCode("ICIC000336");
		account1.setBankName("ICICI Bank");
		
		accountList.add(account);
		accountList.add(account1);
		
		return accountList;
	}
	
	private List<Project> getProjectList(){
		List<Project> projects = new ArrayList<Project>();
		Project project = new Project();
		project.setClient("Maersk Line");
		project.setName("Remote Container Management");
		project.setSize(20);
		
		Project project1 = new Project();
		project1.setClient("Alois Visotchning");
		project1.setName("Ragis");
		project1.setSize(10);
		
		projects.add(project1);
		projects.add(project);
		
		return projects;
	}
	
}
