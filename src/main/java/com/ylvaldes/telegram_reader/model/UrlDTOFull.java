package com.ylvaldes.telegram_reader.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UrlDTOFull {

    private Long id;

    @NotNull
    @Size(max = 1000)
    private String url;

    @NotNull
    private Status status;

    private MercadoDTO Mercado;

    private UrlDataDTO UrlData;

}
