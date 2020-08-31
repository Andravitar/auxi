package com.example.innocv.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.innocv.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
   public Optional<User> findById(String id);
   public List<User> findByName(String name);
   public List<User> findByBirthdate(Date birthdate);
} 