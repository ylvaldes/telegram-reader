package com.ylvaldes.telegram_reader.repos;

import com.ylvaldes.telegram_reader.domain.Registro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegistroRepository extends JpaRepository<Registro, Long> {
}
