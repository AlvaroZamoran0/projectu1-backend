package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name  = "credit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idCredit;

    //details of user
    private String doc;
    private Integer years;
    private Integer type_work; //0->freelance 1->contract

    //financial information
    private Float salary;

    //details of credit
    private Float total_cost;
    private Float amount;
    private Float interest;
    private Integer period;
    private Float quota;
    private String type_credit; // First Home, Second Home, Commercial Properties, Remodelation

    //status
    private Integer status; // step
    private Integer approved; // tracking credit
}
