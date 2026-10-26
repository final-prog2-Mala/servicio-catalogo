package com.programacion.catalogo.controller;

import com.programacion.catalogo.model.Profesional;
import com.programacion.catalogo.model.AgendaSemanal;
import com.programacion.catalogo.repository.ProfesionalRepository;
import com.programacion.catalogo.repository.AgendaSemanalRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    private final ProfesionalRepository profesionalRepository;
    private final AgendaSemanalRepository agendaRepository;

    // Inyección de dependencias por constructor (Buena práctica requerida)
    public ProfesionalController(ProfesionalRepository profesionalRepository, AgendaSemanalRepository agendaRepository) {
        this.profesionalRepository = profesionalRepository;
        this.agendaRepository = agendaRepository;
    }

    @GetMapping
    public List<Profesional> buscarProfesionales(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false, defaultValue = "false") boolean requiereDisponibilidad) {

        // 1. Obtenemos la base de profesionales locales
        List<Profesional> resultados = profesionalRepository.findAll();

        // 2. Filtro obligatorio: Categoría
        if (categoryId != null) {
            resultados = resultados.stream()
                    .filter(p -> p.getCategoryId().equals(categoryId))
                    .collect(Collectors.toList());
        }

        // 3. Filtro obligatorio: Nombre (busca coincidencias en nombre o apellido)
        if (name != null && !name.isBlank()) {
            String busqueda = name.toLowerCase();
            resultados = resultados.stream()
                    .filter(p -> p.getFirstName().toLowerCase().contains(busqueda) || 
                                 p.getLastName().toLowerCase().contains(busqueda))
                    .collect(Collectors.toList());
        }

        // 4. Filtro obligatorio: Estado habilitado
        if (enabled != null) {
            resultados = resultados.stream()
                    .filter(p -> p.getEnabled().equals(enabled))
                    .collect(Collectors.toList());
        }

        // 5. Filtro obligatorio: Disponibilidad (Verifica si tiene reglas de agenda activas en BD local)
        if (requiereDisponibilidad) {
            resultados = resultados.stream()
                    .filter(p -> {
                        List<AgendaSemanal> agendas = agendaRepository.findByProfessionalIdAndEnabledTrue(p.getId());
                        return agendas != null && !agendas.isEmpty();
                    })
                    .collect(Collectors.toList());
        }

        return resultados;
    }
}