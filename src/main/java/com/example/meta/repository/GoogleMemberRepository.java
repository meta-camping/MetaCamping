package com.example.meta.repository;

import com.example.meta.entity.GoogleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
public interface GoogleMemberRepository extends JpaRepository<GoogleMember, Long> {
    Optional<GoogleMember> findByEmailAndProvider(String email, String provider);
}