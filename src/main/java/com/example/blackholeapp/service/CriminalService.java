package com.example.blackholeapp.service;

import com.example.blackholeapp.model.Criminal;
import com.example.blackholeapp.repository.CriminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CriminalService {

    @Autowired
    private CriminalRepository criminalRepository;

    public Criminal addCriminal(Criminal criminal) {
        return criminalRepository.save(criminal);
    }

    public List<Criminal> getAllCriminals() {
        return criminalRepository.findAll();
    }

    public List<Criminal> searchCriminals(String query) {
        return criminalRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }
}
