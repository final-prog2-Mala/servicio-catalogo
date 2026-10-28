package com.programacion.catalogo.controller;

import com.programacion.catalogo.service.KafkaCatalogListener;
import com.programacion.catalogo.service.SincronizacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sincronizacion")
public class SincronizacionController {

    private final SincronizacionService sincronizacionService;
    private final KafkaCatalogListener kafkaCatalogListener;

    public SincronizacionController(SincronizacionService sincronizacionService, KafkaCatalogListener kafkaCatalogListener) {
        this.sincronizacionService = sincronizacionService;
        this.kafkaCatalogListener = kafkaCatalogListener;
    }

    @PostMapping("/ejecutar-snapshot")
    public ResponseEntity<String> forzarSnapshot() {
        try {
            sincronizacionService.ejecutarSnapshot();
            return ResponseEntity.ok("Sincronización completa (Snapshot) ejecutada con éxito.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al sincronizar: " + e.getMessage());
        }
    }

    @PostMapping("/test-incremental")
    public ResponseEntity<String> probarIncremental(@RequestParam String clave) {
        kafkaCatalogListener.escucharCambiosCatalogo(clave);
        return ResponseEntity.ok("Prueba enviada con clave: " + clave);
    }
}