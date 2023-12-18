package com.ar.cac.homebanking.services;

import com.ar.cac.homebanking.exceptions.UserExistsException;
import com.ar.cac.homebanking.exceptions.UserNotExistsException;
import com.ar.cac.homebanking.mappers.UserMapper;
import com.ar.cac.homebanking.models.User;
import com.ar.cac.homebanking.models.dtos.UserDTO;
import com.ar.cac.homebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    // Inyectar una instancia del Repositorio
    @Autowired
    private UserRepository repository;

    // Metodos

    public List<UserDTO> getUsers() {
        // Obtengo la lista de la entidad usuario de la db
        List<User> users = repository.findAll();
        // Mapear cada usuario de la lista hacia un dto
        List<UserDTO> usersDtos = users.stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
        return usersDtos;
    }

    public UserDTO createUser(UserDTO userDto) {
        User userByEmail  = validateUserByEmail(userDto);
        User userByDni  = validateUserByDni(userDto);

        if (userByEmail == null && userByDni  == null){
            User userSaved = repository.save(UserMapper.dtoToUser(userDto));
            return UserMapper.userToDto(userSaved);
        } else {
            throw new UserExistsException("Usuario con mail o con dni existente.");
        }
    }

    public UserDTO getUserById(Long id) {
        // Si el usuario no existe, lanzar UserNotExistsException
        if (repository.existsById(id)) {
            User entity = repository.findById(id).get();
            return UserMapper.userToDto(entity);
        } else {
            throw new UserNotExistsException("Usuario inexistente");
        }
    }

    public void deleteUser(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new UserNotExistsException("Usuario inexistente");
        }
    }

    public UserDTO updateUser(Long id, UserDTO dto) {
        if (repository.existsById(id)) {
            User userToModify = repository.findById(id).get();

            // Verificar que al menos uno de los campos obligatorios esté presente
            if (dto.getName() != null && !dto.getName().isEmpty() ||
                    dto.getSurname() != null && !dto.getSurname().isEmpty() ||
                    dto.getEmail() != null && !dto.getEmail().isEmpty() ||
                    dto.getPassword() != null && !dto.getPassword().isEmpty() ||
                    dto.getDni() != null && !dto.getDni().isEmpty()) {
                // Validar qué datos no vienen en null para setearlos al objeto ya creado

                // Logica del Patch
                if (dto.getName() != null) {
                    userToModify.setName(dto.getName());
                }

                if (dto.getSurname() != null) {
                    userToModify.setSurname(dto.getSurname());
                }

                if (dto.getEmail() != null) {
                    userToModify.setEmail(dto.getEmail());
                }

                if (dto.getPassword() != null) {
                    userToModify.setPassword(dto.getPassword());
                }

                if (dto.getDni() != null) {
                    userToModify.setDni(dto.getDni());
                }

                User userModified = repository.save(userToModify);

                return UserMapper.userToDto(userModified);
            } else {
                // Lanzar una excepción o manejar la situación donde todos los campos son nulos
                throw new IllegalArgumentException("Al menos un campo obligatorio debe estar presente para la actualización.");
            }
        } else {
            // Lanzar la excepción si el usuario no existe
            throw new UserNotExistsException("Usuario inexistente");
        }
    }

    // Validar que existan usuarios unicos por mail
    public User validateUserByEmail(UserDTO dto){
        return repository.findByEmail(dto.getEmail());
    }

    // Validar que existan usuarios unicos por dni
    public User validateUserByDni(UserDTO dto){
        return repository.findByDni(dto.getDni());
    }
}
