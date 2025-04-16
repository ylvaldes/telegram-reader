package com.ylvaldes.telegram_reader.rest;

import com.ylvaldes.leerpdf.service.ProcesarDataService;
import com.ylvaldes.telegram_reader.model.PendingDataResponse;
import com.ylvaldes.telegram_reader.model.Status;
import com.ylvaldes.telegram_reader.model.UrlDTO;
import com.ylvaldes.telegram_reader.model.UrlDTOFull;
import com.ylvaldes.telegram_reader.service.ProcesarService;
import com.ylvaldes.telegram_reader.service.UrlService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/urls", produces = MediaType.APPLICATION_JSON_VALUE)
public class UrlResource {

    private final UrlService urlService;
    private final ProcesarService procesarService;

    public UrlResource(final UrlService urlService, final ProcesarService procesarService) {
        this.urlService = urlService;
        this.procesarService=procesarService;
    }

    @GetMapping
    public ResponseEntity<List<UrlDTOFull>> getAllUrls() {
        return ResponseEntity.ok(urlService.findAll());
    }
    @GetMapping("/status")
    public ResponseEntity<List<UrlDTOFull>> getAllUrlsStatus() {
        return ResponseEntity.ok(urlService.findAllNew());
    }
    @GetMapping("/data")
    public ResponseEntity<List<PendingDataResponse>> findPendingData() throws IOException {
        //procesarService.data();
        return ResponseEntity.ok(urlService.findPendingData());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlDTOFull> getUrl(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(urlService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUrl(@RequestBody @Valid final UrlDTO urlDTO) {
        return new ResponseEntity<>(urlService.create(urlDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUrl(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UrlDTO urlDTO) {
        urlService.update(id, urlDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/setStatus")
    public ResponseEntity<Void> changeStatus(@RequestParam(value = "id") final Long id,
                                             @RequestParam(value ="status") String status) {
        urlService.changeStatus(id, Status.valueOf(status));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUrl(@PathVariable(name = "id") final Long id) {
        urlService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
