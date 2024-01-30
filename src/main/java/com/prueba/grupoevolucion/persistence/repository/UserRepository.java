package com.prueba.grupoevolucion.persistence.repository;

import com.prueba.grupoevolucion.persistence.entity.UserEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndIdNot(String email, Long id);

}
