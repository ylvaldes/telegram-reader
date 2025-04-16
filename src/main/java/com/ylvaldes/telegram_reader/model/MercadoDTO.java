package com.ylvaldes.telegram_reader.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MercadoDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    private Long rut;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    @Size(max = 1000)
    private String urlGetTiket;

}
