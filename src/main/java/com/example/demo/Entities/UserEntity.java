package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name  = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    // Personal information
    private String name;
    private String last_name;
    private Date age;

    // Client information
    private float balance;

    // Account information
    private int rol; // 1: Client   2: Executive    3: Admin
    private String identification;
    private String email;
    private String password;

    public UserEntity(String name, String last_name, Date age, String identification, String email, String password, int rol) {
        this.name = name;
        this.last_name = last_name;
        this.age = age;
        this.identification = identification;
        this.email = email;
        this.password = password;
        this.balance = 0;
        this.rol = rol;
    }
}
