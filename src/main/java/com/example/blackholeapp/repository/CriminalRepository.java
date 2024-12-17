package com.example.blackholeapp.repository;

import com.example.blackholeapp.model.Criminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriminalRepository extends JpaRepository<Criminal, Long> {

    // Vulnerable query
    @Query(value = "SELECT * FROM criminal WHERE 1=1", nativeQuery = true) // allow injection
    List<Criminal> insecureSearchTest(String query);
}
