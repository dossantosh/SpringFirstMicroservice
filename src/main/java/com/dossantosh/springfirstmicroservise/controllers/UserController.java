package com.dossantosh.springfirstmicroservise.controllers;

import com.dossantosh.springfirstmicroservise.common.global.page.Direction;
import com.dossantosh.springfirstmicroservise.common.global.page.KeysetPage;
import com.dossantosh.springfirstmicroservise.models.User;
import com.dossantosh.springfirstmicroservise.projections.dtos.FullUserDTO;
import com.dossantosh.springfirstmicroservise.projections.dtos.UserDTO;
import com.dossantosh.springfirstmicroservise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * GET /api/users
     * Devuelve una página de usuarios con paginación tipo keyset.
     *
     * @param id        (opcional) ID exacto para filtrar
     * @param username  (opcional) comienza con (ignora mayúsculas)
     * @param email     (opcional) comienza con (ignora mayúsculas)
     * @param lastId    (opcional) último ID cargado, para keyset
     * @param limit     (default: 10) cantidad de resultados
     * @param direction (default: NEXT) dirección: NEXT o PREVIOUS
     */
    @GetMapping
    public ResponseEntity<KeysetPage<UserDTO>> getUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "NEXT") String direction) {
        // Validar dirección
        Direction dir;
        try {
            dir = Direction.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        KeysetPage<UserDTO> users = userService.findUsersKeyset(
                id != null ? id : null,
                username != null ? username.toLowerCase() : null,
                email != null ? email.toLowerCase() : null,
                lastId,
                limit,
                dir);

        if (users == null) {
            return ResponseEntity.status(500).body(null); // O lanza excepción
        }

        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/{id}
     * Devuelve un usuario completo con roles, módulos y submódulos
     *
     * @param id ID del usuario a buscar
     * @return FullUserDTO con toda la info del usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<FullUserDTO> getUserDetails(@PathVariable Long id) {
        User user = userService.findFullUserById(id);
        FullUserDTO dto = userService.mapToUserDTO(user);
        return ResponseEntity.ok(dto);
    }
}
