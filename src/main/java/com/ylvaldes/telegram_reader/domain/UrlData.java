package com.ylvaldes.telegram_reader.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class UrlData {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "URL_DATA_sequence",
            sequenceName = "URL_DATA_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "URL_DATA_sequence"
    )
    private Long id;

    @Column(nullable = false, length = 100)
    private String tipoCFE;

    @Column(nullable = false)
    private String serie;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String fecha;

    @Column
    private String codigoSeguridad;

    @Column(nullable = false)
    private Double montoTotal;

    @OneToOne(mappedBy = "idUrlData", fetch = FetchType.LAZY)
    private Url idUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
