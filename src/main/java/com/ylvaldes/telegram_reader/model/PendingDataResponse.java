package com.ylvaldes.telegram_reader.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author YasmaniLedesmaValdez
 * @project telegram-reader
 * @package com.ylvaldes.telegram_reader.model
 * @created 1/5/2023
 * @implNote
 */
@Getter
@Setter
public class PendingDataResponse {
    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    private Long rut;

    @NotNull
    @Size(max = 1000)
    private String urlGetTiket;


    @NotNull
    private Status statusUrl;

    private Long idUrl;


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
