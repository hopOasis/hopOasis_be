package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.PersonalArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalAreaRepository extends JpaRepository<PersonalArea, Long> {
}
