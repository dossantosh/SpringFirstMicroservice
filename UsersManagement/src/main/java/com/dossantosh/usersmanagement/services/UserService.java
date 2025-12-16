package com.dossantosh.usersmanagement.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dossantosh.usersmanagement.common.global.page.Direction;
import com.dossantosh.usersmanagement.common.global.page.KeysetPage;
import com.dossantosh.usersmanagement.common.security.custom.auth.UserAuth;
import com.dossantosh.usersmanagement.common.security.custom.auth.UserAuthProjection;
import com.dossantosh.usersmanagement.common.security.custom.auth.bus.UserAuthDTO;
import com.dossantosh.usersmanagement.models.Modules;
import com.dossantosh.usersmanagement.models.Roles;
import com.dossantosh.usersmanagement.models.Submodules;
import com.dossantosh.usersmanagement.models.User;
import com.dossantosh.usersmanagement.projections.FullUserDTO;
import com.dossantosh.usersmanagement.projections.UserDTO;
import com.dossantosh.usersmanagement.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * Service class that manages users, their roles, modules, and submodules.
 * Provides CRUD operations, paging, mapping between entities and DTOs,
 * and audit logging for user-related actions.
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final ModuleService moduleService;

    private final SubmoduleService submoduleService;

    /**
     * Retrieves all users from the repository.
     * 
     * @return List of all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their ID.
     * 
     * @param id The user ID.
     * @return User entity.
     * @throws EntityNotFoundException if no user found with the given ID.
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    /**
     * Finds user authentication details by username.
     * 
     * @param username The username.
     * @return UserAuthProjection containing authentication data.
     * @throws UsernameNotFoundException if no user found with the given username.
     */
    public UserAuthProjection findUserAuthByUsername(String username) {
        return userRepository.findUserAuthByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Finds a user with full details by ID.
     * 
     * @param id The user ID.
     * @return User entity with full data.
     * @throws EntityNotFoundException if no user found with the given ID.
     */
    public User findFullUserById(Long id) {
        return userRepository.findFullUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    /**
     * Finds a user by username.
     * 
     * @param username The username.
     * @return User entity.
     * @throws UsernameNotFoundException if no user found with the given username.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Finds a user by email.
     * 
     * @param email The user's email.
     * @return User entity.
     * @throws UsernameNotFoundException if no user found with the given email.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Retrieves a paged list of users using keyset pagination.
     * 
     * @param id        Filter by user ID.
     * @param username  Filter by username.
     * @param email     Filter by email.
     * @param lastId    The last ID seen in pagination.
     * @param limit     Maximum number of users to return.
     * @param direction Pagination direction (NEXT or PREVIOUS).
     * @return A KeysetPage containing a list of UserDTO and pagination info.
     */
    public KeysetPage<UserDTO> findUsersKeyset(Long id, String username, String email, Long lastId, int limit,
            Direction direction) {
        // Fetch one extra to detect if more elements exist
        List<UserDTO> users = userRepository.findUsersKeyset(id, username, email, lastId, limit + 1,
                direction.name());

        boolean hasMore = users.size() > limit;
        if (hasMore) {
            users.remove(users.size() - 1); // Remove the extra one
        }

        // Reverse the list if paging backwards to keep ascending order
        if (direction == Direction.PREVIOUS) {
            Collections.reverse(users);
        }

        Long newNextId = null;
        Long newPreviousId = null;
        boolean hasNext = false;
        boolean hasPrevious = false;

        if (!users.isEmpty()) {
            newNextId = users.get(users.size() - 1).getId(); // Last visible user id for next page
            newPreviousId = users.get(0).getId(); // First visible user id for previous page
            if (direction == Direction.NEXT) {
                hasNext = hasMore;
                hasPrevious = lastId != null;
            } else if (direction == Direction.PREVIOUS) {
                hasNext = lastId != null; // Only has next if came from middle
                hasPrevious = hasMore; // Only has previous if extra found in query
            }
        }

        KeysetPage<UserDTO> page = new KeysetPage<>();
        page.setContent(users);
        page.setNextId(newNextId);
        page.setPreviousId(newPreviousId);
        page.setHasNext(hasNext);
        page.setHasPrevious(hasPrevious);

        return page;
    }

    /**
     * Checks if a user exists by ID.
     * 
     * @param id User ID.
     * @return true if user exists, false otherwise.
     */
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * Checks if a user exists by username.
     * 
     * @param username Username.
     * @return true if user exists, false otherwise.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user exists by email.
     * 
     * @param email User's email.
     * @return true if user exists, false otherwise.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Saves or updates a user entity.
     * 
     * @param user User entity to save.
     * @return The saved User entity.
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user by ID with audit logging.
     * 
     * @param id User ID to delete.
     * @throws EntityNotFoundException if user does not exist.
     */
    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }

        userRepository.deleteById(id);
    }

    /**
     * Modifies an existing user with new data, preserving existing values if null
     * or empty.
     * Performs audit logging after modification.
     * 
     * @param user         New user data.
     * @param existingUser Existing user to update.
     */
    public void modifyUser(User user, User existingUser) {

        if (user.getId() == null) {
            user.setId(existingUser.getId());
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(existingUser.getEmail());
        }

        if (user.getEnabled() == null) {
            user.setEnabled(existingUser.getEnabled());
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        }

        if (user.getRoles() == null || user.getModules() == null || user.getSubmodules() == null) {
            return;
        }

        if (user.getRoles().isEmpty() || user.getModules().isEmpty() || user.getSubmodules().isEmpty()) {
            return;
        }
        saveUser(user);
    }

    /**
     * Creates a new user with assigned roles, modules, and submodules based on
     * username.
     * Default roles and modules are assigned as well.
     * Performs audit logging after creation.
     * 
     * @param user User entity to create.
     */
    public void createUser(User user) {

        Set<Roles> roles = new HashSet<>();
        Set<Modules> modules = new HashSet<>();
        Set<Submodules> submodules = new HashSet<>();

        Set<Long> rolesId = new HashSet<>();
        Set<Long> modulesId = new HashSet<>();
        Set<Long> submodulesId = new HashSet<>();

        // Assign roles/modules/submodules based on username for special users
        if (user.getUsername().equals("sevas")) {

            rolesId.add(2L);

            modulesId.add(2L);
            modulesId.add(3L);

            submodulesId.add(2L);
            submodulesId.add(3L);
            submodulesId.add(4L);
            submodulesId.add(5L);
            submodulesId.add(6L);
        }

        if (user.getUsername().equals("dossantosh")) {

            modulesId.add(2L);
            modulesId.add(3L);

            submodulesId.add(2L);
            submodulesId.add(3L);
            submodulesId.add(5L);
            submodulesId.add(6L);
        }

        if (user.getUsername().equals("userprueba")) {
            submodulesId.add(5L);
        }

        // Add default role/module/submodule IDs
        rolesId.add(1L);
        modulesId.add(1L);
        submodulesId.add(1L);

        // Resolve roles by IDs
        Roles role = null;
        for (Long rol : rolesId) {
            if (roleService.existById(rol)) {
                role = roleService.findById(rol);
                roles.add(role);
            }
        }
        // Resolve modules by IDs
        Modules module = null;
        for (Long moduleId : modulesId) {
            if (moduleService.existById(moduleId)) {
                module = moduleService.findById(moduleId);
                modules.add(module);
            }
        }
        // Resolve submodules by IDs
        Submodules submodule = null;
        for (Long submoduleId : submodulesId) {
            if (submoduleService.existById(submoduleId)) {
                submodule = submoduleService.findById(submoduleId);
                submodules.add(submodule);
            }
        }

        if (roles.isEmpty() || modules.isEmpty() || submodules.isEmpty()) {
            return;
        }

        user.setEnabled(true);
        user.setRoles(roles);
        user.setModules(modules);
        user.setSubmodules(submodules);

        saveUser(user);
    }

    /**
     * Loads lists of all roles, modules, and submodules to be used in forms.
     * 
     * @return Map with keys "allRoles", "allModules", and "allSubmodules" and their
     *         respective lists.
     */
    public Map<String, List<?>> permissionsList() {
        Map<String, List<?>> map = new HashMap<>();
        map.put("allRoles", new ArrayList<>(roleService.findAll()));
        map.put("allModules", new ArrayList<>(moduleService.findAll()));
        map.put("allSubmodules", new ArrayList<>(submoduleService.findAll()));
        return map;
    }

    /**
     * Maps a UserAuthProjection to a UserAuth object used for authentication.
     * 
     * @param projection UserAuthProjection containing data.
     * @return UserAuth object or null if input is null.
     */
    public UserAuth mapToUserAuth(UserAuthProjection projection) {

        if (projection == null) {
            return null;
        }

        UserAuth userAuth = new UserAuth();
        userAuth.setId(projection.getId());
        userAuth.setUsername(projection.getUsername());
        userAuth.setEmail(projection.getEmail());
        userAuth.setPassword(projection.getPassword());
        userAuth.setEnabled(projection.getEnabled());

        LinkedHashSet<Long> hashRoles = new LinkedHashSet<>(projection.getRoles());
        userAuth.setRoles(hashRoles);

        LinkedHashSet<Long> hashModules = new LinkedHashSet<>(projection.getModules());
        userAuth.setModules(hashModules);

        LinkedHashSet<Long> hashSubmodules = new LinkedHashSet<>(projection.getSubmodules());
        userAuth.setSubmodules(hashSubmodules);

        return userAuth;
    }

    /**
     * Maps a UserAuth object to a UserAuthDTO data transfer object.
     * 
     * @param userAuth The UserAuth object to map.
     * @return UserAuthDTO or null if the input is null.
     */
    public UserAuthDTO mapToUserAuthDTO(UserAuth userAuth) {
        if (userAuth == null) {
            return null;
        }

        UserAuthDTO dto = new UserAuthDTO();
        dto.setId(userAuth.getId());
        dto.setUsername(userAuth.getUsername());
        dto.setEmail(userAuth.getEmail());
        dto.setEnabled(userAuth.getEnabled());
        dto.setIsAdmin(userAuth.getIsAdmin());

        dto.setRoles(new LinkedHashSet<>(userAuth.getRoles()));
        dto.setModules(new LinkedHashSet<>(userAuth.getModules()));
        dto.setSubmodules(new LinkedHashSet<>(userAuth.getSubmodules()));

        return dto;
    }

    /**
     * Maps a User entity to a FullUserDTO data transfer object.
     * 
     * @param user User entity.
     * @return FullUserDTO or null if user is null.
     */
    public FullUserDTO mapToFullUserDTO(User user) {

        if (user == null) {
            return null;
        }

        FullUserDTO fullUserDTO = new FullUserDTO();
        fullUserDTO.setId(user.getId());
        fullUserDTO.setUsername(user.getUsername());
        fullUserDTO.setEmail(user.getEmail());
        fullUserDTO.setEnabled(user.getEnabled());
        fullUserDTO.setIsAdmin(user.getIsAdmin());

        fullUserDTO.setRoles(new LinkedHashSet<>(user.getRoles()));

        fullUserDTO.setModules(new LinkedHashSet<>(user.getModules()));

        fullUserDTO.setSubmodules(new LinkedHashSet<>(user.getSubmodules()));

        return fullUserDTO;
    }
}
