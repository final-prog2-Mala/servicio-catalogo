package com.programacion.catalogo.repository;

import com.programacion.catalogo.model.AgendaSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgendaSemanalRepository extends JpaRepository<AgendaSemanal, Long> {
    List<AgendaSemanal> findByProfessionalIdAndEnabledTrue(Long professionalId);
}