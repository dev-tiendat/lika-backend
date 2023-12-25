package com.app.lika.service.impl;

import com.app.lika.exception.*;
import com.app.lika.mapper.UserMapper;
import com.app.lika.model.role.Role;
import com.app.lika.model.role.RoleName;
import com.app.lika.model.user.Status;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.pagination.SortBy;
import com.app.lika.payload.pagination.SortOrder;
import com.app.lika.payload.request.UserRequest;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.repository.RoleRepository;
import com.app.lika.repository.UserRepository;
import com.app.lika.repository.specification.UserSpecification;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.app.lika.model.user.User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.app.lika.utils.AppConstants.USER;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_ROLE_NOT_SET = "User role not set";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public PagedResponse<UserProfile> getAllUsers(PaginationCriteria paginationCriteria) {
        SortBy sort = paginationCriteria.getSortBy();
        Pageable pageRequest;
        if (sort.getSortOrder().equals(SortOrder.ASC)) {
            pageRequest = PageRequest.of(paginationCriteria.getPageNumber(),
                    paginationCriteria.getPageSize(),
                    Sort.by(sort.getField()).ascending());
        } else {
            pageRequest = PageRequest.of(paginationCriteria.getPageNumber(),
                    paginationCriteria.getPageSize(),
                    Sort.by(sort.getField()).descending());
        }

        Page<User> users;
        if (!paginationCriteria.isFilterByEmpty() || StringUtils.isNotEmpty(paginationCriteria.getQuery())) {
            UserSpecification userSpecification = new UserSpecification(paginationCriteria.getFilters().getMapOfFilters(), paginationCriteria.getQuery());
            users = userRepository.findAll(userSpecification, pageRequest);
        } else {
            users = userRepository.findAll(pageRequest);
        }

        return new PagedResponse<>(
                users.getContent().stream().map(userMapper::entityToUserProfile).toList(),
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast()
        );
    }

    @Override
    public UserProfile getCurrentUser(UserPrincipal currentUser) {
        User user = userRepository.getUser(currentUser);

        return userMapper.entityToUserProfile(user);
    }

    @Override
    public UserProfile getUserProfile(String username) {
        User user = userRepository.getUserByUsername(username);

        return userMapper.entityToUserProfile(user);
    }

    @Override
    public UserProfile addUser(UserRequest userRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userRequest.getUsername()))) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists", ExceptionCustomCode.USERNAME_ALREADY_EXISTS.getCode());
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(userRequest.getEmail()))) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists", ExceptionCustomCode.EMAIL_ALREADY_EXISTS.getCode());
        }

        List<RoleName> rolesRequest = userRequest.getRoles();
        if ((rolesRequest.contains(RoleName.ROLE_ADMIN) && rolesRequest.contains(RoleName.ROLE_STUDENT)
                || (rolesRequest.contains(RoleName.ROLE_STUDENT) && rolesRequest.contains(RoleName.ROLE_TEACHER)))) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Role request is invalid");
        }
        List<Role> roles = new ArrayList<>();
        for (RoleName role : rolesRequest) {
            roles.add(roleRepository.findByName(role)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        }

        String username = userRequest.getUsername().trim();
        String password = passwordEncoder.encode(userRequest.getPassword().trim());
        String email = userRequest.getEmail().trim();
        String phoneNumber = "";
        String firstName = userRequest.getFirstName().trim();
        String lastName = userRequest.getLastName().trim();
        String address = userRequest.getAddress().trim();

        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .firstName(firstName)
                .lastName(lastName)
                .gender(userRequest.getGender())
                .dateOfBirth(userRequest.getDateOfBirth())
                .address(address)
                .status(Status.ACTIVE)
                .build();
        user.setRoles(roles);

        return userMapper.entityToUserProfile(userRepository.save(user));
    }

    @Override
    public UserProfile deleteUser(String username) {
        User user = userRepository.getUserByUsername(username);
        if (!user.getRoles().contains(roleRepository.findByName(RoleName.ROLE_ADMIN).get())) {
            try {
                userRepository.delete(user);

                return userMapper.entityToUserProfile(user);
            } catch (Exception ex) {
                throw new IntegrityConstraintViolationException(USER);
            }
        }

        throw new UnauthorizedException("You cannot delete admin users");
    }

    @Override
    public UserProfile giveAdmin(String username) {
        User user = userRepository.getUserByUsername(username);
        List<Role> roles = user.getRoles();
        if (roles.contains(roleRepository.findByName(RoleName.ROLE_TEACHER).get())
                && !roles.contains(roleRepository.findByName(RoleName.ROLE_ADMIN).get())
                && roles.size() == 1) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
            user.setRoles(roles);

            return userMapper.entityToUserProfile(userRepository.save(user));
        }

        throw new BadRequestException("You don't give role to user: " + user.getUsername());
    }

    @Override
    public UserProfile updateUser(UserRequest newUser, String username, UserPrincipal currentUser) {
        User user = userRepository.getUserByUsername(username);

        if (user.getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()).trim());
            user.setAddress(newUser.getAddress());
            user.setPhoneNumber(newUser.getPhoneNumber());
            user.setGender(newUser.getGender());
            user.setDateOfBirth(newUser.getDateOfBirth());
            user.setEmail(newUser.getEmail());

            return userMapper.entityToUserProfile(userRepository.save(user));
        }

        throw new UnauthorizedException("You don't have permission to update profile of :" + username);
    }

    @Override
    public UserProfile activateOrDeactivateUser(String username, Status status) {
        User user = userRepository.getUserByUsername(username);
        user.setStatus(status);

        return userMapper.entityToUserProfile(userRepository.save(user));
    }

}
