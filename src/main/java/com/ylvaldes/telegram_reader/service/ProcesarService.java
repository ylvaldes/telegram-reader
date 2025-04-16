package com.ylvaldes.telegram_reader.service;

import com.ylvaldes.leerpdf.service.ProcesarDataService;
import com.ylvaldes.leerpdf.utiles.LoadResourceConfLeerPDF;
import com.ylvaldes.telegram_reader.model.PendingDataResponse;
import com.ylvaldes.telegram_reader.model.Status;
import com.ylvaldes.telegram_reader.util.ObtenerPdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author YasmaniLedesmaValdez
 * @project telegram-reader
 * @package com.ylvaldes.telegram_reader.service
 * @created 22/4/2023
 * @implNote
 */
@Service
public class ProcesarService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UrlService urlService;

    @Autowired
    ObtenerPdf obtenerPdf;

    public ProcesarService() {
        this.procesarDataService = new ProcesarDataService();
    }

    ProcesarDataService procesarDataService;

    public void data() throws IOException {
        LoadResourceConfLeerPDF recurso = new LoadResourceConfLeerPDF();
        recurso.loadResourceConf();
        List<PendingDataResponse> lista = urlService.findPendingData();

        for (PendingDataResponse pendingDataResponse : lista) {
            try {
                obtenerPdf.getPdf(pendingDataResponse.getUrlGetTiket(), pendingDataResponse.getNombre(), pendingDataResponse);
                urlService.changeStatus(pendingDataResponse.getIdUrl(), Status.PROCESSED);
            }catch (RuntimeException runtimeException){
                urlService.changeStatus(pendingDataResponse.getIdUrl(), Status.PENDING);
            }

        }

    }

}
