package com.example.innocv.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.innocv.model.User;
import com.example.innocv.service.UserService;


@RestController
@RequestMapping("/api")   // http://localhost:8080/api/
public class UserController {

	@Autowired
	private UserService userService; 
	
	@GetMapping("/users")      // http://localhost:8080/api/users
	public List<User> getAll(){
		return userService.getAll();
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUser(@PathVariable String id) {
		
		Optional<User> user = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			user = userService.getById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(user == null) {
			response.put("mensaje", "El usuario ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<User>(user.get(), HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<?> addUser(@Valid @RequestBody User user, BindingResult result) {
		
		User newUser = null;
		Map<String, Object> response = new HashMap<>();
		//System.out.println(user.getName());
		//System.out.println(user.getBirthdate());
		
		if(result.hasErrors()) {//comprobar si el json perteneciente al objeto "user" tiene un formato correcto

			//Por cada fieldError que encuentre, convierte su tipo en un String, y despues se construye una lista con todos sus elementos convertidos
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			if(user.getName() == null || user.getBirthdate() == null) {
				response.put("mensaje", "El campo name o birthday del cuerpo de la petición = null, se requiere de datos!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);		
			}
				
			newUser = userService.create(user);
			
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El usuario ha sido creado con éxito!");
		response.put("usuario", newUser);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult result, @PathVariable String id) {

		Optional<User> actualUser = userService.getById(id);
		User updatedUser = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (actualUser == null) {
			response.put("mensaje", "Error: no se pudo editar, el usuario ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			actualUser.get().setName(user.getName());
			actualUser.get().setBirthdate(user.getBirthdate());

			updatedUser = userService.update(id, actualUser.get());

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el usuario en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El usuario ha sido actualizado con éxito!");
		response.put("usuario", updatedUser);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Optional<User> user = userService.getById(id);
			
			if(user == null) {
				response.put("mensaje", "Error: no se pudo eliminar, el usuario ID: "
						.concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			
		    userService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el usuario de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/users")
	public ResponseEntity<?> deleteAll() {
		
		Map<String, Object> response = new HashMap<>();
		
		userService.deleteAll();
			
		if(!userService.getAll().isEmpty()) {
			response.put("mensaje", "Error al eliminar los usuarios de la base de datos");				
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
					
		response.put("mensaje", "Los usuarios se han eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
	}
	
	

	
}
