package com.ylvaldes.telegram_reader.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UrlDataDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String tipoCFE;

    @NotNull
    @Size(max = 255)
    private String serie;

    @NotNull
    @Size(max = 255)
    private String numero;

    @NotNull
    private String fecha;

    @Size(max = 255)
    private String codigoSeguridad;

    @NotNull
    private Double montoTotal;

}
