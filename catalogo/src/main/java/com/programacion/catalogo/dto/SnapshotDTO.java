package com.programacion.catalogo.dto;

import com.programacion.catalogo.model.Categoria;
import com.programacion.catalogo.model.Profesional;
import com.programacion.catalogo.model.AgendaSemanal;
import java.time.LocalDateTime;
import java.util.List;

public class SnapshotDTO {
    private Long snapshotVersion;
    private LocalDateTime generatedAt;
    private List<Categoria> professionalCategories;
    private List<Profesional> professionals;
    private List<AgendaSemanal> weeklySchedules;

    // Getters y Setters
    public Long getSnapshotVersion() { return snapshotVersion; }
    public void setSnapshotVersion(Long snapshotVersion) { this.snapshotVersion = snapshotVersion; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public List<Categoria> getProfessionalCategories() { return professionalCategories; }
    public void setProfessionalCategories(List<Categoria> professionalCategories) { this.professionalCategories = professionalCategories; }
    public List<Profesional> getProfessionals() { return professionals; }
    public void setProfessionals(List<Profesional> professionals) { this.professionals = professionals; }
    public List<AgendaSemanal> getWeeklySchedules() { return weeklySchedules; }
    public void setWeeklySchedules(List<AgendaSemanal> weeklySchedules) { this.weeklySchedules = weeklySchedules; }
}