package com.ylvaldes.telegram_reader.repos;

import com.ylvaldes.telegram_reader.domain.Mercado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MercadoRepository extends JpaRepository<Mercado, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByRut(Long rut);

    Optional<Mercado> findByRut(Long rut);

}
