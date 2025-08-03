package com.example.SaaSApplication.user.repository;

import com.example.SaaSApplication.entity.TblUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TblUserDetailsRepository extends JpaRepository<TblUserDetails, Long> {
    Optional<TblUserDetails> findByEmail(String email);
}
