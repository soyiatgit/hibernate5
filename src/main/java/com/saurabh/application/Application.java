package com.saurabh.application;

import java.util.Scanner;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

// if your repositories, components and services are not in the same package
// as this file; then you need todefine the below annotation to specify the
// packages for picking up dependencies automatically
@EnableAutoConfiguration
@ComponentScan("com.saurabh")
public class Application {

	public static void main(String[] args) {
		//replaceStrings();
		// getSQLVersion();
		//SpringApplication.run(Application.class, args);
		System.out.println("The Max integer is :: " + Integer.MAX_VALUE);
		System.out.println("Adding one to the Max integer is :: " + (1 + Integer.MAX_VALUE));
	}
	
	
	 /**
	  * just a method to test the Hibernate Session
	  */
	 /*
	  private static void getSQLVersion() {
		try(Session session = HibernateUtil.getSessionFactory().openSession();) {
			String query = "Select version()";
			String version = (String) session.createNativeQuery(query).getSingleResult();
			System.out.println("Current SQL version :: " + version);
		} catch(Exception ex) {
			System.out.println("Error occured while getting the version of SQL");
		}
	}
	*/
	
	private static void replaceStrings() {
		Scanner stream = new Scanner(System.in);
		String source = stream.next(); 
		String target = stream.next();
		
		// make one identical to two
		if(null == source || null == target) {
			throw new NullPointerException("Expected string one but found none");
		}
		
		if(target.isEmpty()) {
			System.out.println(source.length());
		}
		
		if(source.isEmpty()) {
			System.out.println(target.length());
		}
		
		char[] target$ = target.toCharArray();
		char[] source$ = source.toCharArray();
		int count = 0;
		int cycles = 0;
		int diff = 0;
		for(int i=0; i < source$.length && i < target$.length; i++) {
			if(!(target$[i] == source$[i])) {
				count++;
			}
			cycles++;
		}
		
		if(cycles < target$.length) {
			diff = target$.length - cycles;
		}
		
		if(cycles < source$.length) {
			diff = source$.length - cycles;
		}
		
		System.out.println(count + diff);
	}

}
