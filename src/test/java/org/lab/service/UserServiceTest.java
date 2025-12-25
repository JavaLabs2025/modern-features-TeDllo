package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.TestBase;
import org.lab.model.User;
import org.lab.repository.inmemory.InMemoryUserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends TestBase {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegister() {
        var user = userService.register("John Doe");

        assertNotNull(user);
        assertNotNull(user.id());
        assertEquals("John Doe", user.name());
        assertNotNull(user.createdAt());
        assertTrue(user.createdAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testRegisterMultipleUsers() {
        var user1 = userService.register("User1");
        var user2 = userService.register("User2");

        assertNotEquals(user1.id(), user2.id());
        assertEquals("User1", user1.name());
        assertEquals("User2", user2.name());
    }
}

