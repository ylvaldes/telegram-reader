package com.ylvaldes.telegram_reader.service;

import com.ylvaldes.telegram_reader.model.MercadoDTO;
import com.ylvaldes.telegram_reader.model.Status;
import com.ylvaldes.telegram_reader.model.UrlDTO;
import com.ylvaldes.telegram_reader.model.UrlDataDTO;
import com.ylvaldes.telegram_reader.util.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author YasmaniLedesmaValdez
 * @project telegram-reader
 * @package com.ylvaldes.telegram_reader.service
 * @created 22/4/2023
 * @implNote
 */
@Component
public class ProcesarURLService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UrlService urlService;

    @Autowired
    UrlDataService urlDataService;

    @Autowired
    MercadoService mercadoService;

    public String procesarDatos(String urlInicial) {
        String respuesta = "";
        logger.debug(getClass().getSimpleName() + " - " + Thread.currentThread().getStackTrace()[1].getMethodName() + "- Begin; URL: {}", urlInicial);

        String url = urlInicial.substring(47, urlInicial.length());
        List<String> result = Arrays.asList(url.split("\\s*,\\s*"));
        logger.debug("Dtos Obtenido de la URL: {}", result.toString());

        MercadoDTO mercadoDTO = new MercadoDTO();
        //Buscamos el Mercado Si no Existe lo Creamos
        try {
            mercadoDTO = mercadoService.getByRut(Long.valueOf(result.get(0)));
        } catch (NotFoundException e) {
            logger.warn(getClass().getSimpleName() + " - " + Thread.currentThread().getStackTrace()[1].getMethodName() + "NotFoundException: {}", e.getStackTrace());
            mercadoDTO.setRut(Long.valueOf(result.get(0)));
            mercadoDTO.setNombre(result.get(0));
            mercadoDTO.setUrlGetTiket("https://cfe.rondanet.com/cgi-bin/Publicacion.cgi");
            mercadoDTO.setId(mercadoService.create(mercadoDTO));
        }

        if (!urlService.urlExists(urlInicial)) {

            try {
                //Se crean los datos de la URL
                UrlDataDTO urlDataDTO = new UrlDataDTO();
                urlDataDTO.setTipoCFE(result.get(1));
                urlDataDTO.setSerie(result.get(2));
                urlDataDTO.setNumero(result.get(3));
                urlDataDTO.setMontoTotal(Double.valueOf(result.get(4)));
                urlDataDTO.setFecha(result.get(5));
                urlDataDTO.setCodigoSeguridad(result.get(6).substring(0, 6));

                Long idUrlData = urlDataService.create(urlDataDTO);

                //Se crea la URL
                UrlDTO urlDTO = new UrlDTO();
                urlDTO.setUrl(urlInicial);
                urlDTO.setStatus(Status.NEW);
                urlDTO.setIdUrlData(idUrlData);
                urlDTO.setIdMercado(mercadoDTO.getId());

                Long idUrl = urlService.create(urlDTO);
                respuesta += "[";
                respuesta += "Solicitud: id= " + idUrl;
                respuesta += " ";
                respuesta += " Mercado= " + mercadoDTO.getNombre();
                respuesta += "]";
            } catch (Exception e) {
                logger.error(getClass().getSimpleName() + " - " + Thread.currentThread().getStackTrace()[1].getMethodName() + "Exception: {}", e.getMessage());
                e.getStackTrace();
            }
        } else {
            respuesta += "[";
            respuesta += "Solicitud: Existente";
            respuesta += "]";
        }
        logger.debug(getClass().getSimpleName() + " - " + Thread.currentThread().getStackTrace()[1].getMethodName() + "- End; Respuesta: {}", respuesta);

        return respuesta;
    }
}
