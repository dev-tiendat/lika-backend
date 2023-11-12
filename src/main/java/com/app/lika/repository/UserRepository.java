package com.app.lika.repository;

import com.app.lika.exception.ResourceNotFoundException;
import com.app.lika.model.user.User;
import com.app.lika.security.UserPrincipal;
import com.app.lika.utils.AppConstants;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Override
    Page<User> findAll (Specification specification, Pageable pageable);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(@NotBlank String email);

    boolean existsByUsername(@NotBlank String username);

    boolean existsByEmail(@NotBlank String email);

    void deleteById(@NotBlank Long id);

    Optional<User> findByUsernameOrEmail(String username, String email);

    default User getUser(UserPrincipal currentUser) {
        return getUserByUsername(currentUser.getUsername());
    }

    default User getUserByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER, AppConstants.USERNAME, username));
    }
}
