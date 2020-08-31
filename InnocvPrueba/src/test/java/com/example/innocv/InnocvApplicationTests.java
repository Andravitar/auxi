package com.example.innocv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.innocv.model.User;
import com.example.innocv.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InnocvApplicationTests {

	@Autowired
    UserRepository personRepository;

    @Test
    public void contextLoads() {}
    
    @BeforeEach
	public void init() {
    	if(!personRepository.findAll().isEmpty()) {
    		personRepository.deleteAll();
    	}
	}

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
    
    @Test
    public void addNewUserToDb() throws ParseException {
    	
        User user = new User();
        user.setName("Ana");
        
        String fechaEnString = "2010-01-02";
        Date date = formatter.parse(fechaEnString);
        user.setBirthdate(date);
        
        assertEquals(personRepository.findAll().size(), 0);
        
        personRepository.save(user);

        assertEquals(personRepository.findByBirthdate(date).get(0).getName(), "Ana");
        assertEquals(personRepository.findByName(user.getName()).get(0).getBirthdate(), date);
        assertEquals(personRepository.findAll().size(), 1);
        
    }
    
    @Test
    public void addAndDeleteUserFromDb() throws ParseException {
    	
        User user = new User();
        user.setName("Ana");
        
        String fechaEnString = "2010-01-02";
        Date date = formatter.parse(fechaEnString);
        user.setBirthdate(date);
                
        personRepository.save(user);
        assertEquals(personRepository.findAll().size(), 1);
 
        personRepository.deleteById(personRepository.findAll().get(0).getId());       
        assertEquals(personRepository.findAll().size(), 0);        
    }
    
    @Test
    public void addAndUpdateNameUserFromDb() throws ParseException {
    	
        User user = new User();
        user.setName("Ana");
        
        String fechaEnString = "2010-01-02";
        Date date = formatter.parse(fechaEnString);
        user.setBirthdate(date);
                
        personRepository.save(user);
        assertEquals(personRepository.findAll().get(0).getName(), "Ana");
        
        user.setName("Alex");
        personRepository.save(user);
        assertEquals(personRepository.findAll().get(0).getName(), "Alex");
 
        
    }
    
    @Test
    public void addAndUpdateBirthdateUserFromDb() throws ParseException {
    	
        User user = new User();
        user.setName("Ana");
        
        String fechaEnString1 = "2010-01-02";
        Date date1 = formatter.parse(fechaEnString1);
        user.setBirthdate(date1);
                
        personRepository.save(user);
        assertEquals(personRepository.findAll().get(0).getBirthdate(), date1);
        
        String fechaEnString2 = "2020-01-02";
        Date date2 = formatter.parse(fechaEnString2);
        user.setBirthdate(date2);
        
        personRepository.save(user);
        assertEquals(personRepository.findAll().get(0).getBirthdate(), date2);
 
    }
    
    @Test
    public void add2AndDeleleAllFromDb() throws ParseException {
    	
        User user1 = new User();
        user1.setName("Ana");
        
        String fechaEnString1 = "2010-01-02";
        Date date1 = formatter.parse(fechaEnString1);
        user1.setBirthdate(date1);
                
        personRepository.save(user1);
        
        User user2 = new User();
        user2.setName("Alex");
        
        String fechaEnString2 = "2020-01-02";
        Date date2 = formatter.parse(fechaEnString2);
        user2.setBirthdate(date2);
                
        personRepository.save(user2);
        
        
        assertEquals(personRepository.findAll().size(), 2);
        
        personRepository.deleteAll();
        
        assertEquals(personRepository.findAll().size(), 0);

    }
    

}
