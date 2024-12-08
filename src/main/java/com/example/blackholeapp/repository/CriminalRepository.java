package com.example.blackholeapp.repository;

import com.example.blackholeapp.model.Criminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriminalRepository extends JpaRepository<Criminal, Long> {

    List<Criminal> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
