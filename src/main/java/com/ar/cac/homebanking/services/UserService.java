package com.ar.cac.homebanking.services;

import com.ar.cac.homebanking.exceptions.UserExistsException;
import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.mappers.UserMapper;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    // Inyectar una instancia del Repositorio
    @Autowired
    private UserRepository repository;

    // Metodos

    public List<UserDTO> getUsers(){
        // Obtengo la lista de la entidad usuario de la db
        List<User> users = repository.findAll();
        // Mapear cada usuario de la lista hacia un dto
         List<UserDTO> usersDtos = users.stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
        return usersDtos;
    }

    public UserDTO createUser(UserDTO userDto) throws UserExistsException {
        User userValidated = validateUserByEmail(userDto);

        if (userValidated == null){
            User userSaved = repository.save(UserMapper.dtoToUser(userDto));
            return UserMapper.userToDto(userSaved);
        } else{
            throw new UserExistsException("Usuario con mail: " + userDto.getEmail() + " ya existe");
        }

    }


    public UserDTO getUserById(Long id) {
        User entity = repository.findById(id).get();
        return UserMapper.userToDto(entity);
    }

    public void deleteUser(Long id){
       if (repository.existsById(id)){
           repository.deleteById(id);
        } else {
           throw new UserNotExistsException("Usuario inexistente");
       }

    }



    public UserDTO updateUser(Long id, UserDTO dto) {
        if (repository.existsById(id)){
            User userToModify = repository.findById(id).get();
            // Validar qué datos no vienen en null para setearlos al objeto ya creado

            // Logica del Patch
            if (dto.getName() != null){
                userToModify.setName(dto.getName());
            }

            if (dto.getSurname() != null){
                userToModify.setSurname(dto.getSurname());
            }

            if (dto.getEmail() != null){
                userToModify.setEmail(dto.getEmail());
            }

            if (dto.getPassword() != null){
                userToModify.setPassword(dto.getPassword());
            }

            if (dto.getDni() != null){
                userToModify.setDni(dto.getDni());
            }

            User userModified = repository.save(userToModify);

            return UserMapper.userToDto(userModified);
        }

        return null;
    }

    // Validar que existan usuarios unicos por mail
    public User validateUserByEmail(UserDTO dto){
        return repository.findByEmail(dto.getEmail());
    }

    // Validar que existan usuarios unicos por mail
/*
    public User validateUserByEmail2(UserDTO dto){
       try {
           //Instrucciones cuando no hay exepcion
           return repository.findByEmail(dto.getEmail());
       } catch (Exception e) { //IOException ex?
            // Instrucciones cuando se produce una excepcion
               System.out.println("Escriba un mail válido");

        }

    }

 */
}


