package com.ylvaldes.telegram_reader.util;

import com.ylvaldes.telegram_reader.model.PendingDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import com.ylvaldes.telegram_reader.mercados.Tata;

/**
 * @author YasmaniLedesmaValdez
 * @project telegram-reader
 * @package com.ylvaldes.telegram_reader.util
 * @created 1/5/2023
 * @implNote
 */
@Service
public class ObtenerPdf {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${app.output}")
    private String output;
    private static final String EXTENSION = ".pdf";



    public void getPdf(String url, String mercado, PendingDataResponse pendingDataResponse) throws IOException {
        File file = new File(output + File.separator + mercado + EXTENSION);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = getHttpHeaders();
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("RUT", String.valueOf(pendingDataResponse.getRut()));
        map.add("TipoCFE", pendingDataResponse.getTipoCFE());
        map.add("Serie", pendingDataResponse.getSerie());
        map.add("Numero", pendingDataResponse.getNumero());
        map.add("Total", String.valueOf(pendingDataResponse.getMontoTotal()));
        map.add("Hash6", pendingDataResponse.getCodigoSeguridad());

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(map, requestHeaders);
        ResponseEntity<String> result =
                restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if(result.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException();

        if(result.getStatusCode() == HttpStatus.OK && result.getBody().contains("Datos no válidos"))
            throw new RuntimeException();

        byte[] bytes = result.getBody().getBytes();
        Path path = Paths.get(file.getAbsolutePath());
        //Files.write(path, bytes,StandardOpenOption.CREATE ,StandardOpenOption.WRITE );

        FileOutputStream outputStream =
                new FileOutputStream(path.getFileName().toString());
        outputStream.write(bytes);
        Tata tata = new Tata();
        tata.leerDatos(path.getFileName().toString(), output,mercado);
    }
    private HttpHeaders getHttpHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_PDF,MediaType.APPLICATION_OCTET_STREAM));
        return requestHeaders;
    }
}
