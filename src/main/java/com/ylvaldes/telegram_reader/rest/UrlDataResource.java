package com.ylvaldes.telegram_reader.rest;

import com.ylvaldes.telegram_reader.model.UrlDataDTO;
import com.ylvaldes.telegram_reader.service.UrlDataService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/urlDatas", produces = MediaType.APPLICATION_JSON_VALUE)
public class UrlDataResource {

    private final UrlDataService urlDataService;

    public UrlDataResource(final UrlDataService urlDataService) {
        this.urlDataService = urlDataService;
    }

    @GetMapping
    public ResponseEntity<List<UrlDataDTO>> getAllUrlDatas() {
        return ResponseEntity.ok(urlDataService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlDataDTO> getUrlData(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(urlDataService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUrlData(@RequestBody @Valid final UrlDataDTO urlDataDTO) {
        return new ResponseEntity<>(urlDataService.create(urlDataDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUrlData(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UrlDataDTO urlDataDTO) {
        urlDataService.update(id, urlDataDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUrlData(@PathVariable(name = "id") final Long id) {
        urlDataService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
