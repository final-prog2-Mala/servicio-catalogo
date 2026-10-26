package com.programacion.catalogo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "version_catalogo")
public class VersionCatalogo {

    @Id
    private Long id = 1L; // Solo tendremos un registro que se actualiza

    private Long version;
    private LocalDateTime ultimaSincronizacion;

    public VersionCatalogo() {}

    public VersionCatalogo(Long version, LocalDateTime ultimaSincronizacion) {
        this.id = 1L;
        this.version = version;
        this.ultimaSincronizacion = ultimaSincronizacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public LocalDateTime getUltimaSincronizacion() { return ultimaSincronizacion; }
    public void setUltimaSincronizacion(LocalDateTime ultimaSincronizacion) { this.ultimaSincronizacion = ultimaSincronizacion; }
}