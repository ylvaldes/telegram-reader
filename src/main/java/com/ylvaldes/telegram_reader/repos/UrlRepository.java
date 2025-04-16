package com.ylvaldes.telegram_reader.repos;

import com.ylvaldes.telegram_reader.domain.Url;
import com.ylvaldes.telegram_reader.model.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UrlRepository extends JpaRepository<Url, Long> {

    boolean existsByUrlIgnoreCase(String url);

    List<Url> findAllByStatus(Status status);

}
