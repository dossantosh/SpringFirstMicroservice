package com.dossantosh.usersmanagement.services;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dossantosh.usersmanagement.common.security.custom.auth.UserAuth;
import com.dossantosh.usersmanagement.common.security.custom.auth.UserAuthProjection;
import com.dossantosh.usersmanagement.models.User;
import com.dossantosh.usersmanagement.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private ModuleService moduleService;

    @Mock
    private SubmoduleService submoduleService;

    @Mock
    private UserAuth userAuth;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");

        verify(userRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with ID 99 not found");

        verify(userRepository).findById(99L);
    }

    @Test
    void shouldReturnUserById() {
        User user = new User();
        user.setId(10L);
        user.setUsername("john");

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        User result = userService.findById(10L);

        assertThat(result.getUsername()).isEqualTo("john");

        verify(userRepository).findById(10L);
    }

    @Test
    void shouldSaveUser() {
        User user = new User();
        user.setUsername("newuser");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertThat(result.getUsername()).isEqualTo("newuser");
        verify(userRepository).save(user);
    }

    @Test
    void shouldDeleteUserById() {
        User user = new User();
        user.setUsername("newuser");
        Long id = 42L;

        user.setId(id);
        userService.saveUser(user);
        userService.deleteById(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldCheckIfUserExistsByEmail() {
        String email = "user@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean exists = userService.existsByEmail(email);

        assertThat(exists).isTrue();
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void shouldFindUserAuthByUsername() {
        String username = "authuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserAuthProjection userAuthProjection = userService.findUserAuthByUsername(username);

        UserAuth auth = userService.mapToUserAuth(userAuthProjection);

        assertThat(auth).isNotNull();
        assertThat(auth.getUsername()).isEqualTo(username);

        verify(userRepository).findByUsername(username);
    }

    @Test
    void shouldThrowIfUserAuthNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserAuthByUsername("unknown"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("not found");

        verify(userRepository).findByUsername("unknown");
    }
}
