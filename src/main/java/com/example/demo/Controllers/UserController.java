package com.example.demo.Controllers;


import com.example.demo.Entities.UserEntity;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/identification/{identification}")
    public ResponseEntity<UserEntity> getUserByIdentification(@PathVariable String identification) {
        UserEntity user = userService.getUserByIdentification(identification);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/sing_in")
    public ResponseEntity<UserEntity> sign_in(@RequestBody UserEntity user) {
        UserEntity new_user = userService.sign_in(user.getName(),user.getLast_name(), user.getAge(), user.getIdentification(), user.getEmail(), user.getPassword(), user.getRol());
        return ResponseEntity.ok(new_user);
    }

    @GetMapping("/login")
    public ResponseEntity<UserEntity> login(@RequestParam("identification") String identification, @RequestParam("password") String password) {
        UserEntity user = userService.login(identification, password);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/get_age/{identification}")
    public ResponseEntity<Integer> calculateAge(@PathVariable String identification) {
        Integer age = userService.calculateAge(identification);
        return ResponseEntity.ok(age);
    }

    @PutMapping("update_account/{identification}/{value}")
    public ResponseEntity<UserEntity> updateAccount(@PathVariable String identification, @PathVariable Float value) {
        UserEntity u = userService.updateAccount(identification, value);
        return ResponseEntity.ok(u);
    }

}