package com.ylvaldes.telegram_reader.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Mercado {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "MERCADO_sequence",
            sequenceName = "MERCADO_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MERCADO_sequence"
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = true)
    private Long rut;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, length = 1000)
    private String urlGetTiket;

    @OneToMany(mappedBy = "idMercado")
    private Set<Url> urls;

    @OneToMany(mappedBy = "idMercado")
    private Set<Registro> registros;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
