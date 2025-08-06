package com.dossantosh.usersmanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dossantosh.usersmanagement.common.global.page.Direction;
import com.dossantosh.usersmanagement.common.global.page.KeysetPage;
import com.dossantosh.usersmanagement.models.User;
import com.dossantosh.usersmanagement.projections.FullUserDTO;
import com.dossantosh.usersmanagement.projections.UserDTO;
import com.dossantosh.usersmanagement.services.UserService;

/**
 * REST controller for managing user-related operations.
 *
 * <p>
 * This controller provides endpoints to:
 * <ul>
 * <li>Retrieve a paginated list of users using keyset pagination</li>
 * <li>Get the full details of a specific user by ID</li>
 * </ul>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a list of users using keyset pagination, with optional filtering by
     * ID, username, or email.
     *
     * @param id        (optional) Exact user ID to filter by
     * @param username  (optional) Username starts with (case-insensitive)
     * @param email     (optional) Email starts with (case-insensitive)
     * @param lastId    (optional) Last loaded ID for keyset pagination
     * @param limit     Maximum number of results to return (default: 50)
     * @param direction Pagination direction: "NEXT" or "PREVIOUS" (default: NEXT)
     * @return {@link ResponseEntity} containing a {@link KeysetPage} of
     *         {@link FullUserDTO},
     *         or 400 Bad Request if direction is invalid, or 500 Internal Server
     *         Error if service fails
     */
    @GetMapping
    public ResponseEntity<KeysetPage<UserDTO>> getUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "NEXT") String direction) {

        Direction dir;
        try {
            dir = Direction.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Invalid direction
        }

        KeysetPage<UserDTO> users = userService.findUsersKeyset(
                id,
                username != null ? username.toLowerCase() : null,
                email != null ? email.toLowerCase() : null,
                lastId,
                limit,
                dir);

        if (users == null) {
            return ResponseEntity.status(500).body(null); // Internal error
        }

        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves the full details of a user including roles, modules, and
     * submodules.
     *
     * @param id The ID of the user to retrieve
     * @return {@link ResponseEntity} with {@link FullUserDTO} containing full
     *         user
     *         information,
     *         or 200 OK if found, or 404 if the user does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<FullUserDTO> getUserDetails(@PathVariable Long id) {
        User user = userService.findFullUserById(id);
        FullUserDTO dto = userService.mapToUserDTO(user);
        return ResponseEntity.ok(dto);
    }
}
