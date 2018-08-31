package com.saurabh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saurabh.entities.Animal;
import com.saurabh.services.AnimalService;

@RestController
@RequestMapping("/api/v1/Animal")
public class AnimalController {
	
	@Autowired
	AnimalService animalService;
	
	@GetMapping()
	public void saveAnimal() {
		animalService.saveAnimal();
	}
	
	@GetMapping("/{id}")
	public Animal getAnimal(@PathVariable("id") Integer id) {
		return animalService.getAnimal(id);
	}
}
