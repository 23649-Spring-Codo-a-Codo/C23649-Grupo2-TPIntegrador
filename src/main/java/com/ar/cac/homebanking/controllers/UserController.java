package com.ar.cac.homebanking.controllers;

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

    // GET USER BY ID con GlobalExceptionHandler (Alejandra)

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(service.getUserById(id));
    }

    // CREATE USER con GlobalExceptionHandler (Alejandra)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user){
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(user));
    }

    // UPDATE USER con GlobalExceptionHandler (Alejandra)
    @PutMapping(value="/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
        service.updateUser(id, user);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario modificado correctamente");
    }

    // DELETE USER con GlobalExceptionHandler (Alejandra)
    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        service.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario borrado correctamente");
    }
}