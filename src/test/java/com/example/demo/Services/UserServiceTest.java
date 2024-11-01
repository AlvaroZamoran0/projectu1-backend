package com.example.demo.Services;

import com.example.demo.Entities.UserEntity;
import com.example.demo.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inicializar un UserEntity para las pruebas
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.JANUARY, 1);
        user = new UserEntity("John", "Doe", calendar.getTime(), "12345", "john.doe@example.com", "password", 1);
    }

    @Test
    void testGetUserByIdentification() {
        when(userRepository.findByIdentification("12345")).thenReturn(user);

        UserEntity foundUser = userService.getUserByIdentification("12345");
        assertNotNull(foundUser);
        assertEquals("John", foundUser.getName());
    }

    @Test
    void testSignInSuccess() {
        when(userRepository.findByIdentification("12345")).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity newUser = userService.sign_in("John", "Doe", new Date(), "12345", "john.doe@example.com", "password", 1);
        assertNotNull(newUser);
        assertEquals("John", newUser.getName());
    }

    @Test
    void testSignInUserExists() {
        when(userRepository.findByIdentification("12345")).thenReturn(user);

        UserEntity newUser = userService.sign_in("John", "Doe", new Date(), "12345", "john.doe@example.com", "password", 1);
        assertNull(newUser); // No debe permitir el registro si ya existe
    }

    @Test
    void testSignInEmptyName() {
        when(userRepository.findByIdentification("12345")).thenReturn(null);

        UserEntity newUser = userService.sign_in("", "Doe", new Date(), "12345", "john.doe@example.com", "password", 1);
        assertNull(newUser); // No debe permitir registro si el nombre está vacío
    }

    @Test
    void testSignInEmptyLastName() {
        when(userRepository.findByIdentification("12345")).thenReturn(null);

        UserEntity newUser = userService.sign_in("John", "", new Date(), "12345", "john.doe@example.com", "password", 1);
        assertNull(newUser); // No debe permitir registro si el apellido está vacío
    }

    @Test
    void testSignInEmptyIdentification() {
        when(userRepository.findByIdentification("12345")).thenReturn(null);

        UserEntity newUser = userService.sign_in("John", "Doe", new Date(), "", "john.doe@example.com", "password", 1);
        assertNull(newUser); // No debe permitir registro si la identificación está vacía
    }

    @Test
    void testSignInEmptyEmail() {
        when(userRepository.findByIdentification("12345")).thenReturn(null);

        UserEntity newUser = userService.sign_in("John", "Doe", new Date(), "12345", "", "password", 1);
        assertNull(newUser); // No debe permitir registro si el email está vacío
    }

    @Test
    void testSignInEmptyPassword() {
        when(userRepository.findByIdentification("12345")).thenReturn(null);

        UserEntity newUser = userService.sign_in("John", "Doe", new Date(), "12345", "john.doe@example.com", "", 1);
        assertNull(newUser); // No debe permitir registro si la contraseña está vacía
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByIdentification("12345")).thenReturn(user);

        UserEntity loggedUser = userService.login("12345", "password");
        assertNotNull(loggedUser);
        assertEquals("John", loggedUser.getName());
    }

    @Test
    void testLoginInvalidCredentials() {
        when(userRepository.findByIdentification("12345")).thenReturn(user);

        UserEntity loggedUser = userService.login("12345", "wrongpassword");
        assertNull(loggedUser); // Debe devolver null para credenciales inválidas
    }

    @Test
    void testCalculateAge() {
        when(userRepository.findByIdentification("12345")).thenReturn(user);

        Integer age = userService.calculateAge("12345");
        assertEquals(34, age); // Asumiendo que el año actual es 2024
    }

    @Test
    void testCalculateAge_BeforeBirthday() {
        // Cambiamos la fecha de nacimiento a hoy
        user.setAge(new Date()); // Hoy, así que la edad debería ser 0

        when(userRepository.findByIdentification("12345")).thenReturn(user);

        Integer age = userService.calculateAge("12345");
        assertEquals(0, age); // Debe ser 0 si es el cumpleaños
    }

    @Test
    void testCalculateAge_AfterBirthday() {
        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.set(1985, Calendar.JANUARY, 1); // Fecha de nacimiento en el pasado
        user.setAge(pastCalendar.getTime());

        when(userRepository.findByIdentification("12345")).thenReturn(user);

        Integer age = userService.calculateAge("12345");
        assertEquals(39, age); // Asumiendo que el año actual es 2024
    }

    @Test
    void testCalculateAge_BeforeBirthdayNotYet() {
        // Establecer una fecha de nacimiento en el pasado que no haya ocurrido este año
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.set(1990, Calendar.DECEMBER, 31); // Nació el 31 de diciembre de 1990
        user.setAge(birthCalendar.getTime());

        when(userRepository.findByIdentification("12345")).thenReturn(user);

        Integer age = userService.calculateAge("12345");
        assertEquals(33, age); // Asumiendo que hoy es el 1 de noviembre de 2024, su edad debe ser 33
    }

    @Test
    void testUpdateAccount() {
        user.setBalance(100.0f);
        when(userRepository.findByIdentification("12345")).thenReturn(user);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity updatedUser = userService.updateAccount("12345", 50.0f);
        assertNotNull(updatedUser);
        assertEquals(150.0f, updatedUser.getBalance()); // Verifica el nuevo saldo
    }
}
