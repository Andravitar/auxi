package com.example.innocv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.innocv.model.User;
import com.example.innocv.repository.UserRepository;
 

@Service
public class UserService {
	
	@Autowired
	private UserRepository personRepository;
	
	
	public List<User> getAll(){
		return personRepository.findAll();
	}
	
	
	public Optional<User> getById(String id) {
		return personRepository.findById(id);
	}

	
	public User create(User user) {
		return personRepository.save(user);
	}
	
	
	public void deleteAll() {
		personRepository.deleteAll();
	}
	
	public void delete(String id) {
		Optional<User> u = personRepository.findById(id);
		personRepository.delete(u.get());
	}
	
	public User update (String id, User user) {
		Optional<User> u = personRepository.findById(id);
		u.get().setName(user.getName());
		u.get().setBirthdate(user.getBirthdate());
		return personRepository.save(u.get());
	}

	
}
