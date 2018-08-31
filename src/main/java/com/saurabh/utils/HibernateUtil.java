package com.saurabh.utils;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import com.saurabh.entities.Account;
import com.saurabh.entities.Animal;
import com.saurabh.entities.Cat;
import com.saurabh.entities.Dog;
import com.saurabh.entities.Employee;
import com.saurabh.entities.Family;
import com.saurabh.entities.Project;

/**
 * this file shows two method of configuring the hibernate in our application.
 * If we do not wish to work with XML, we can opt for the Map based approach.
 * @author saurabhtiwari
 *
 */

public class HibernateUtil {

	private static StandardServiceRegistry standardServiceRegistry;
	
	protected  static SessionFactory sessionFactory;
	
	/*static {
		try {
			if(null == standardServiceRegistry) {
				
				// since we have the hibernate setting file with the name hibernate.cfg.xml, which
				// is the default name, we do not need to pass the name below. If the file name is
				// something else we need to pass the name to configure method below.
				standardServiceRegistry = new StandardServiceRegistryBuilder().configure().build();
				MetadataSources metaDataSources = new MetadataSources(standardServiceRegistry);
				metaDataSources.addPackage("com.saurabh.entities");
				Metadata metaData = metaDataSources.getMetadataBuilder().build();
				sessionFactory = metaData.getSessionFactoryBuilder().build();
			}
		} catch (Exception e) {
			System.out.println("Error occured while trying to get the session factory");
			if(null != standardServiceRegistry) {
				StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
			}
		}
	}*/
	
	static {
		try {
			if(null == standardServiceRegistry) {
				
				//the following map is an alternative to the hibernate.cfg.xml file
				Map<String, String> dbSettings = new HashMap<>();
				dbSettings.put(Environment.URL, "jdbc:postgresql://localhost:5432/saurabh");
				dbSettings.put(Environment.USER, "postgres");
				dbSettings.put(Environment.PASS, "root");
				dbSettings.put(Environment.DRIVER, "org.postgresql.Driver");
				dbSettings.put(Environment.SHOW_SQL, "true");
				dbSettings.put(Environment.HBM2DDL_AUTO, "create");
				dbSettings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL94Dialect");
				dbSettings.put(Environment.NON_CONTEXTUAL_LOB_CREATION, "true");
				
				// the rest of the code is same as with the xml file
				standardServiceRegistry = new StandardServiceRegistryBuilder().applySettings(dbSettings).build();
				MetadataSources metaDataSources = new MetadataSources(standardServiceRegistry);
				
				// this way we need to tell hibernate about all our entities. There is no way to tell the entire
				// entity package. This way is the alternative to the mapping tag in the hibernate.cfg.xml file.
				metaDataSources.addAnnotatedClass(Employee.class);
				metaDataSources.addAnnotatedClass(Family.class);
				metaDataSources.addAnnotatedClass(Account.class);
				metaDataSources.addAnnotatedClass(Project.class);
				metaDataSources.addAnnotatedClass(Animal.class);
				metaDataSources.addAnnotatedClass(Dog.class);
				metaDataSources.addAnnotatedClass(Cat.class);
				
				Metadata metaData = metaDataSources.getMetadataBuilder().build();
				sessionFactory = metaData.getSessionFactoryBuilder().build();
			}
		} catch (Exception e) {
			System.out.println("Error occured while trying to get the session factory");
			if(null != standardServiceRegistry) {
				StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
			}
		}
	}
	
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
}
