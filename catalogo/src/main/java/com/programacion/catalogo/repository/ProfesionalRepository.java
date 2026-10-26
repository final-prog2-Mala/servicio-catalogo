package com.programacion.catalogo.repository;

import com.programacion.catalogo.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
    List<Profesional> findByCategoryIdAndEnabledTrue(Long categoryId);
    List<Profesional> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Profesional> findByEnabled(Boolean enabled);
}