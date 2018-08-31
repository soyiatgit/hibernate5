package com.saurabh.services;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.saurabh.entities.Animal;
import com.saurabh.entities.Cat;
import com.saurabh.entities.Dog;
import com.saurabh.utils.HibernateUtil;

@Service
public class AnimalService {

	public void saveAnimal() {
		Animal animal = new Animal();
		
		Dog dog = new Dog();
		dog.setName("Tommy");
		dog.setColor("Brown");
		dog.setFood("Bone");
		
		Cat cat = new Cat();
		cat.setName("Shuru");
		cat.setFood("Milk");
		cat.setSound("Meow");
		
		try(Session session = HibernateUtil.getSessionFactory().openSession();) {
			/**
			 * Understanding inheritance in Hibernate:
			 ****************************************************************************************
			 * 1. Single Table Strategy
			 * Since our Animal class is the base class and Dog and Cat extends from it.
			 * Hibernate by default creates a single table using the animal definition
			 * and add all the extra columns from Dog and Cat to the same table. (You
			 * specifcally need to make a check for instanceOf while retrieving the values
			 * back from Database.)
			 * 
			 * Depending on which values are set in each instance the same table is
			 * populated leaving the unapplicable fields as NULL
			 * 
			 * By Default, Hibernate creates a column named DTYPE (Distinction-Type)
			 * and inserts the class name against it so that various rows from the same 
			 * table can be identified against there actual class.
			 * 
			 * Ex: for Dog row this value will be Dog and Cat for the Cat row.
			 * 
			 * This default column name and value can however be modified as shown in entities
			 *****************************************************************************************
			 * 2. Table Per Class Strategy
			 * As we might notice the tables created in Single Table strategy are not
			 * normalized. We can instruct Hibernate to create a table for each of our class
			 * by defining @Inheritance(strategy=InheritanceType.SINGLE_TABLE) in base entity.
			 * 
			 * Hibernate will now generate one table for base class and one for each child class.
			 * The Discrimator Column and Type are obviously not required now.
			 * 
			 * Also this strategy doesn't work with IDENTITY generator for Id in Base entity, so 
			 * use AUTO strategy instead.
			 * 
			 * Another important thing is that the properties from the base class are all repeated
			 * in to the child class table. So this strategy is also not fully normalized.
			 ******************************************************************************************
			 * 3. Joined Strategy
			 * In case of Join strategy Hibernate creates one table each for each class,
			 * the common fields from the base class are saved in the base class table while 
			 * the specific table contains the variables for the specific classes.
			 * 
			 * The relation is mapped by the primary key of base table mapped to their counterpart
			 * in specific tables.
			 * 
			 */ 
			session.beginTransaction();
			session.save(animal);
			session.save(dog);
			session.save(cat);
			session.getTransaction().commit();
		} catch(Exception ex) {
			System.out.println("Error occured while trying to save the animal" + ex.getMessage());
		}
		
	}

	/**
	 * while retreiving the data from database, we won't know which instance the given id belongs to,
	 * means we do not know if it will be a Dog, a cat etc. So always we need to fetch it as a Base
	 * class. The hibernate based on the type of Inheritance used will automatically fire a relevant
	 * query and return us a Base class object. We can then check for the instance of that object 
	 * @param id
	 */
	public Animal getAnimal(Integer id) {
		Animal animal = null;
		try(Session session = HibernateUtil.getSessionFactory().openSession();) {
			session.beginTransaction();
			animal = session.get(Animal.class, id);
			if(animal instanceof Animal && !(animal instanceof Cat) &&  !(animal instanceof Dog)) {
				System.out.println("We know its a Animal");
			} else if(animal instanceof Animal && (animal instanceof Cat)) {
				System.out.println("We know its a Cat");
				Cat cat = (Cat) animal;
			} else if(animal instanceof Animal && (animal instanceof Dog)) {
				System.out.println("We know its a Dog");
				Dog dog = (Dog) animal;
			}
		} catch(Exception ex) {
			System.out.println("Error occured while fetching the Animal from DB.");
		}
		
		return animal;
	}

}
