package com.example.hop_oasis.repository;

import com.example.hop_oasis.dto.UserForAdminResponse;
import com.example.hop_oasis.dto.UserResponse;
import com.example.hop_oasis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT new com.example.hop_oasis.dto.UserResponse(u.email, u.firstName, u.lastName) 
            FROM User u
            WHERE u.id = :id
             """)
    Optional<UserResponse> findUserById(Long id);

    @Query("""
            SELECT new com.example.hop_oasis.dto.UserForAdminResponse(u.id, u.email, u.firstName, u.lastName, u.role) 
            FROM User u
             """)
    Optional<List<UserForAdminResponse>> findAllUsers();
}
