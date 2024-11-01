package com.example.demo.Controllers;

import com.example.demo.Entities.CreditEntity;
import com.example.demo.Services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/credit")
@CrossOrigin
public class CreditController {
    @Autowired
    CreditService creditService;

    @GetMapping("/{get_credit}")
    public ResponseEntity<CreditEntity> getCredit(@PathVariable Long get_credit) {
        CreditEntity credit = creditService.getCredit(get_credit);
        return ResponseEntity.ok(credit);
    }

    @GetMapping("/pre_approved/{approved}")
    public ResponseEntity<List<CreditEntity>> getByApproved(@PathVariable Integer approved) {
        List<CreditEntity> credits = creditService.getByApproved(approved);
        return ResponseEntity.ok(credits);
    }

    @GetMapping("/status/{doc}")
    public ResponseEntity<List<CreditEntity>> getUserCredits(@PathVariable String doc) {
        List<CreditEntity> credits = creditService.getUserCredits(doc);
        return ResponseEntity.ok(credits);
    }

    @PostMapping("/saveCredit")
    public ResponseEntity<CreditEntity> saveCredit(@RequestBody CreditEntity credit) {
        CreditEntity C = creditService.saveCredit(credit.getDoc(), credit.getYears(), credit.getType_work(), credit.getSalary(),credit.getAmount(),
                credit.getInterest(), credit.getPeriod(), credit.getType_credit());
        return ResponseEntity.ok(C);
    }

    @PostMapping("/simulate/{amount}/{interest}/{period}")
    public ResponseEntity<Float> simulate_credit(@PathVariable("amount") Float amount, @PathVariable("interest") Float interest, @PathVariable("period") Integer period) {
        Float value = creditService.simulate_credit(amount, interest, period);
        return ResponseEntity.ok(value);
    }

    @PostMapping("/step_1/{id}")
    public ResponseEntity<Float> step1_credit(@PathVariable("id") Long id) {
        Float S1 = creditService.step1_credit(id);
        return ResponseEntity.ok(S1);
    }

    @PostMapping("/step_3/{id}")
    public ResponseEntity<String> step3_credit(@PathVariable("id") Long id) {
        String S3 = creditService.step3_credit(id);
        return ResponseEntity.ok(S3);
    }

    @PostMapping("/step_4/{id}/{value}")
    public ResponseEntity<Float> step4_credit(@PathVariable("id") Long id, @PathVariable("value") Integer value) {
        Float S4 = creditService.step4_credit(id, value);
        return ResponseEntity.ok(S4);
    }

    @PostMapping("/step_5/{id}/{property_amount}")
    public ResponseEntity<String> step5_credit(@PathVariable("id") Long id, @PathVariable("property_amount") Float property_amount) {
        String S5 = creditService.step5_credit(id, property_amount);
        return ResponseEntity.ok(S5);
    }

    @PostMapping("/step_6/{age}/{period}")
    public ResponseEntity<String> step6_credit(@PathVariable("age") Integer age, @PathVariable Integer period) {
        String S6 = creditService.step6_credit(age,period);
        return ResponseEntity.ok(S6);
    }

    @PostMapping("/step_7/{ticks}")
    public ResponseEntity<String> step7_credit(@PathVariable("ticks") Integer ticks) {
        String S7 = creditService.step7_credit(ticks);
        return ResponseEntity.ok(S7);
    }

    @PutMapping("next_step/{id}")
    public ResponseEntity<CreditEntity> next_step(@PathVariable("id") Long id) {
        CreditEntity credit = creditService.next_step(id);
        return ResponseEntity.ok(credit);
    }

    @PutMapping("tracking/{id}/{type}")
    public ResponseEntity<CreditEntity> tracking(@PathVariable("id") Long id, @PathVariable("type") Integer type) {
        CreditEntity credit = creditService.tracking(id, type);
        return ResponseEntity.ok(credit);
    }

    @PutMapping("total_cost/{id}/{amount}/{interest}/{period}")
    public ResponseEntity<CreditEntity> total_cost(@PathVariable("id") Long id, @PathVariable("amount") Float amount, @PathVariable("interest") Float interest, @PathVariable("period") Integer period) {
        CreditEntity credit = creditService.total_cost(id, amount, interest, period);
        return ResponseEntity.ok(credit);
    }
}