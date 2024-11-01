package com.example.demo.Services;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserEntity getUserByIdentification(String identification) {
        return userRepository.findByIdentification(identification);
    }

    public UserEntity sign_in(String name, String lastname, Date age, String identification, String email, String password, Integer rol) {
        UserEntity user = new UserEntity(name, lastname, age, identification, email, password, rol);
        UserEntity existingUser = userRepository.findByIdentification(identification);
        if (existingUser != null || name.isEmpty() || lastname.isEmpty() || identification.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return null;
        }
        return userRepository.save(user);
    }


    public UserEntity login(String identification, String password) {
        UserEntity user = getUserByIdentification(identification);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // invalid credencials
    }

    public Integer calculateAge(String identification) {
        UserEntity u = getUserByIdentification(identification);
        Date birth = u.getAge();  // birth almacena la fecha de nacimiento como Date

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birth);

        Calendar today = Calendar.getInstance();

        // Calculate age in years
        int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

        // if the birthdate not yet
        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    public UserEntity updateAccount(String identification, Float amount) {
        UserEntity u = userRepository.findByIdentification(identification);
        Float balance = u.getBalance();
        u.setBalance(balance + amount);
        return userRepository.save(u);
    }

}
