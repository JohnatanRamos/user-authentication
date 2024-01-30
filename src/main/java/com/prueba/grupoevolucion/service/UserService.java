package com.prueba.grupoevolucion.service;

import com.prueba.grupoevolucion.persistence.entity.UserEntity;
import com.prueba.grupoevolucion.persistence.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Object saveUser(UserEntity user) throws BadRequestException {

        if (this.getUserByEmail(user.getEmail()) != null) {
            throw new BadRequestException("El email ya existe");
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public Object updateUser(UserEntity user) throws BadRequestException {
        Optional<UserEntity> userDb = this.userRepository.findById(user.getId());
        Optional<UserEntity> userEmailExist = this.userRepository.findByEmailAndIdNot(user.getEmail(), user.getId());
        if (userEmailExist.isEmpty() && userDb.isPresent()) {
            String password = userDb.get().getPassword();
            user.setPassword(password);
            return this.userRepository.save(user);
        } else {
            throw new BadRequestException("El email ya existe");
        }
    }

    public UserEntity getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    public Boolean changePassword(String newPassword, String oldPassword, Long idUser) throws BadRequestException {
        UserEntity user = this.userRepository.findById(idUser).orElse(null);

        if (user != null) {
            boolean isValid = this.passwordEncoder.matches(oldPassword, user.getPassword());

            if (!isValid) {
                throw new BadRequestException("La contraseña no es correcta");
            }

            user.setPassword(this.passwordEncoder.encode(newPassword));
            this.userRepository.save(user);
            return true;
        }
        throw new BadRequestException("El usuario no existe");
    }

}
