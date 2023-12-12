package com.ar.cac.homebanking.controllers;

import com.ar.cac.homebanking.exceptions.UserExistsException;
import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.mappers.UserMapper;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // Generar una instancia del Service (UserService) -> Inyectar una instancia mediante Spring Boot
    @Autowired
    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    // Para cada método HTTP permitido debemos realizar una acción
    // Definir el DTO y el Service (Inyección de Dependencia)

    // CRUD: Crear, Leer, Modificar, Eliminar


    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<UserDTO> lista = service.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    //GET USER by ID anterior (sin try-catch). Borrar cuando el cambio esté aprobado
/*
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
    }
*/

    // GET USER BY ID con try-catch (Alejandra)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        try {
            service.getUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
        } catch (UserNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + e.getMessage());
        }

    }



    //CREATE USER anterior (sin try-catch). Borrar cuando el cambio esté aprobado
    /*
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user){
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(user));
    }
*/
    //CREATE USER con try-catch (Alejandra)
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO user){
        try {
            UserDTO userCreated = service.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
        } catch (UserExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    // UPDATE USER anterior (sin try-catch). Borrar cuando el cambio esté aprobado
    /*
    @PutMapping(value="/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO user){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateUser(id, user));
    }
*/
    //UPDATE USER con try-catch (Alejandra)
    @PutMapping(value="/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
        try {
            service.updateUser(id, user);
            return ResponseEntity.status(HttpStatus.OK).body("Usuario modificado correctamente");
        } catch (UserNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario a modificar no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: no se pueden enviar datos nulos: " + e.getMessage());
        }

    }


    //DELETE USER anterior (sin try-catch). Borrar cuando el cambio esté aprobado
    /*
    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deleteUser(id));
    }
*/
    //DELETE USER con try-catch (Alejandra)
    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        try {
            service.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("Usuario borrado correctamente");
        } catch (UserNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario a borrar no encontrado: " + e.getMessage());
        }

    }

}
