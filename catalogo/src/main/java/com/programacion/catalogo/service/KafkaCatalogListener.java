package com.programacion.catalogo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programacion.catalogo.model.Profesional;
import com.programacion.catalogo.repository.ProfesionalRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaCatalogListener {

    private final StringRedisTemplate redisTemplate;
    private final ProfesionalRepository profesionalRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Se instancia directamente

    public KafkaCatalogListener(StringRedisTemplate redisTemplate, 
                                ProfesionalRepository profesionalRepository) {
        this.redisTemplate = redisTemplate;
        this.profesionalRepository = profesionalRepository;
    }

    @KafkaListener(topics = "${catedra.kafka.topic.catalog}", groupId = "${spring.kafka.consumer.group-id}")
    public void escucharCambiosCatalogo(String mensaje) {
        System.out.println("=========================================");
        System.out.println("¡AVISO DE KAFKA RECIBIDO!");
        System.out.println("Clave a buscar en Redis: " + mensaje);

        try {
            String datosJson = redisTemplate.opsForValue().get(mensaje);

            if (datosJson != null) {
                System.out.println("JSON traído desde Redis: " + datosJson);
                Profesional profesional = objectMapper.readValue(datosJson, Profesional.class);
                profesionalRepository.save(profesional);
                System.out.println("¡Base de Datos local actualizada con éxito para: " + profesional.getId() + "!");
            } else {
                System.out.println("Consulta exitosa a Redis, pero la clave no contenía datos (null).");
            }
            System.out.println("=========================================");

        } catch (Exception e) {
            System.err.println("Error procesando actualización incremental: " + e.getMessage());
        }
    }
}