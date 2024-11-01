package com.example.demo.Services;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Repositories.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.pow;

@Service
public class CreditService {
    @Autowired
    CreditRepository creditRepository;

    public CreditEntity getCredit(Long id) {
        return creditRepository.findByIdCredit(id);
    }

    public List<CreditEntity> getUserCredits(String n_doc) {
        return creditRepository.findByDoc(n_doc);
    }

    public List<CreditEntity> getByApproved(Integer approved) {
        return creditRepository.findByApprovedIsLessThan(approved);
    }

    public CreditEntity saveCredit(String n_doc, Integer years, Integer type_work, float salary, float amount, float interest, int period, String type_credit) {
        Float quota = simulate_credit(amount, interest, period);
        CreditEntity C = new CreditEntity(null, n_doc, years, type_work, salary, null, amount, interest, period, quota, type_credit, 1, 1);
        return creditRepository.save(C);
    }

    public Float simulate_credit(float amount, float interest, int period) {
        int n = period * 12;
        float r = interest / 12 / 100;
        Float M = (float) (amount * (r * pow(1 + r, n)) / (pow(1 + r, n) - 1));
        return M;
    }

    public Float step1_credit(Long id) {
        CreditEntity C = getCredit(id);
        Float q = C.getQuota();
        Float s =  C.getSalary();
        Float relation = (q / s) * 100;
        return relation;
    }

    public String step3_credit(Long id) {
        CreditEntity C = getCredit(id);
        Integer years = C.getYears();
        Integer type_work = C.getType_work();
        if (type_work == 1 && years >= 1) {
            return "Todo en orden";
        }
        if (type_work == 1 && years < 1) {
            return "No aplica para crédito";
        } else {
            return "Revisar documentos";
        }
    }

    public Float step4_credit(Long id, Integer value) {
        CreditEntity C = getCredit(id);
        Float s = C.getSalary();
        Float q = C.getQuota();
        Float debt = q + value;
        Float relation = (debt / s) * 100;
        return relation;
    }

    public String step5_credit(Long id, Float property_amount) {
        CreditEntity C = getCredit(id);
        Float a = C.getAmount();
        String t = C.getType_credit();
        Float property = (property_amount/a) * 100;
        String res = "";
        switch (t) {
            case "First Home":
                if (property <= 80) {
                    res = "Porcentaje de financiamiento: " + property + "%, Se puede financiar el proyecto";
                } else {
                    res = "Porcentaje de financiemiento: " + property + "%, No puede financiar el proyecto";
                }
                break;

            case "Second Home":
                if (property <= 70) {
                    res = "Porcentaje de financiamiento: " + property + "%, Se puede financiar el proyecto";
                } else {
                    res = "Porcentaje de financiemiento: " + property + "%, No puede financiar el proyecto";
                }
                break;

            case "Commercial Properties":
                if (property <= 60) {
                    res = "Porcentaje de financiamiento: " + property + "%, Se puede financiar el proyecto";
                } else {
                    res = "Porcentaje de financiemiento: " + property + "%, No puede financiar el proyecto";
                }
                break;

            case "Remodelation":
                if (property <= 50) {
                    res = "Porcentaje de financiamiento: " + property + "%, Se puede financiar el proyecto";
                } else {
                    res = "Porcentaje de financiemiento: " + property + "%, No puede financiar el proyecto";
                }
                break;
        }
        return res;
    }

    public String step6_credit(Integer age, Integer period) {
        if ( age >= 18 && age+period <= 75){
            return age + ": Cumple con el requerimiento de edad";
        } else {
            return age + ": No cumple con el requerimiento de edad";
        }
    }

    public String step7_credit(Integer ticks) {
        if (ticks == 5 ){
            return "Capacidad de ahorro sólida";
        } else if (ticks >= 3) {
            return "Capacidad de ahorro moderada";
        } else {
            return "Capacidad de ahorro insuficiente";
        }
    }

    public CreditEntity next_step(Long id){
        CreditEntity C = getCredit(id);
        int step = C.getStatus();
        step = step + 1;
        C.setStatus(step);
        return creditRepository.save(C);
    };

    public CreditEntity tracking(Long id, Integer type){
        CreditEntity C = getCredit(id);
        int tracking = C.getApproved();
        if (type == 1) {
            C.setApproved(tracking + 1); //current normal
        } else if (type == 2) {
            C.setApproved(5); // denied
        } else {
            C.setApproved(6); // canceled
        }
        return creditRepository.save(C);
    }

    public CreditEntity total_cost(Long id, Float amount, Float interest, Integer period){
        CreditEntity C = getCredit(id);
        Float quota  = simulate_credit(amount, interest, period);
        Double extra = amount * 0.0003;
        Double extra1 = amount * 0.01;
        Float new_quota = (float) (quota + extra + 20000);
        Float total = (float) (new_quota*12*period + extra1);
        C.setQuota(new_quota);
        C.setTotal_cost(total);
        return creditRepository.save(C);
    }
}
