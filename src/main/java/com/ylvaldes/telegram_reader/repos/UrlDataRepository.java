package com.ylvaldes.telegram_reader.repos;

import com.ylvaldes.telegram_reader.domain.UrlData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UrlDataRepository extends JpaRepository<UrlData, Long> {
}
