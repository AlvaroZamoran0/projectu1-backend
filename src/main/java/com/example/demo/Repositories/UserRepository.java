package com.example.demo.Repositories;

import com.example.demo.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findByIdentification(String identification);
    public UserEntity findByEmail(String email);
}
