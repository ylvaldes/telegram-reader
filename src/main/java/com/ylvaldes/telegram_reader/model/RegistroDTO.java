package com.ylvaldes.telegram_reader.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistroDTO {

    private Long id;

    @NotNull
    private Double importe;

    @NotNull
    @Size(max = 255)
    private String moneda;

    @NotNull
    @Size(max = 255)
    private String categoria;

    @NotNull
    private LocalDateTime fecha;

    @NotNull
    @Size(max = 255)
    private String descripcion;

    @NotNull
    @Size(max = 255)
    private String beneficiario;

    @NotNull
    @Size(max = 255)
    private String direccion;

    private Long idUrl;

    private Long idMercado;

}
