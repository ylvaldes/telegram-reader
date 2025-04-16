package com.ylvaldes.telegram_reader.domain;

import com.ylvaldes.telegram_reader.model.Status;
import jakarta.persistence.*;

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
public class Url {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "URL_sequence",
            sequenceName = "URL_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "URL_sequence"
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 1000)
    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mercado_id")
    private Mercado idMercado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_url_data_id", unique = true)
    private UrlData idUrlData;

    @OneToMany(mappedBy = "idUrl")
    private Set<Registro> registros;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
