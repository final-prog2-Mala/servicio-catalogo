package com.programacion.catalogo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaCatalogListener {

    private final SincronizacionService sincronizacionService;
    private final ObjectMapper objectMapper;

    public KafkaCatalogListener(SincronizacionService sincronizacionService) {
        this.sincronizacionService = sincronizacionService;
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @KafkaListener(topics = "${catedra.kafka.topic.catalog}", groupId = "${spring.kafka.consumer.group-id}")
    public void escucharCambiosCatalogo(String mensaje) {
        System.out.println("=========================================");
        System.out.println("¡AVISO DE KAFKA RECIBIDO!");
        System.out.println("Evento crudo: " + mensaje);

        try {
            JsonNode evento = objectMapper.readTree(mensaje);
            String eventType = evento.path("eventType").asText();
            long newVersion = evento.path("newVersion").asLong();
            String eventId = evento.path("eventId").asText();

            System.out.println("Tipo: " + eventType + " | Nueva versión: " + newVersion + " | EventID: " + eventId);

            if ("CatalogUpdated".equals(eventType)) {
                sincronizacionService.ejecutarSnapshot();
                System.out.println("Catálogo actualizado localmente tras evento versión " + newVersion);
            }
            System.out.println("=========================================");

        } catch (Exception e) {
            System.err.println("Error procesando evento de Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }
}