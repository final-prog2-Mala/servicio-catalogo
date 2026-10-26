package com.programacion.catalogo.repository;

import com.programacion.catalogo.model.VersionCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionCatalogoRepository extends JpaRepository<VersionCatalogo, Long> {
}