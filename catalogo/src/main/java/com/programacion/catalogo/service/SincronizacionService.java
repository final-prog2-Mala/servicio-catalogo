package com.programacion.catalogo.service;

import com.programacion.catalogo.dto.SnapshotDTO;
import com.programacion.catalogo.model.VersionCatalogo;
import com.programacion.catalogo.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import java.time.LocalDateTime;

@Service
public class SincronizacionService {

    private final RestClient restClient;
    private final CategoriaRepository categoriaRepository;
    private final ProfesionalRepository profesionalRepository;
    private final AgendaSemanalRepository agendaRepository;
    private final VersionCatalogoRepository versionRepository;

    @Value("${catedra.api.url}")
    private String apiUrl;

    @Value("${catedra.api.token}")
    private String apiToken;

    public SincronizacionService(RestClient restClient,
                                 CategoriaRepository categoriaRepository,
                                 ProfesionalRepository profesionalRepository,
                                 AgendaSemanalRepository agendaRepository,
                                 VersionCatalogoRepository versionRepository) {
        this.restClient = restClient;
        this.categoriaRepository = categoriaRepository;
        this.profesionalRepository = profesionalRepository;
        this.agendaRepository = agendaRepository;
        this.versionRepository = versionRepository;
    }

    @Transactional
    public void ejecutarSnapshot() {
        // 1. Descargar Snapshot completo
        SnapshotDTO snapshot = restClient.get()
                .uri(apiUrl + "/api/synchronization/snapshot")
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .body(SnapshotDTO.class);

        if (snapshot != null) {
            agendaRepository.deleteAllInBatch();
            profesionalRepository.deleteAllInBatch();
            categoriaRepository.deleteAllInBatch();

            categoriaRepository.saveAll(snapshot.getProfessionalCategories());
            profesionalRepository.saveAll(snapshot.getProfessionals());
            agendaRepository.saveAll(snapshot.getWeeklySchedules());

            VersionCatalogo version = versionRepository.findById(1L).orElse(new VersionCatalogo());
            version.setVersion(snapshot.getSnapshotVersion());
            version.setUltimaSincronizacion(LocalDateTime.now());
            versionRepository.save(version);
        }
    }
}