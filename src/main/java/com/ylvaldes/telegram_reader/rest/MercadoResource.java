package com.ylvaldes.telegram_reader.rest;

import com.ylvaldes.telegram_reader.model.MercadoDTO;
import com.ylvaldes.telegram_reader.service.MercadoService;
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
@RequestMapping(value = "/api/mercados", produces = MediaType.APPLICATION_JSON_VALUE)
public class MercadoResource {

    private final MercadoService mercadoService;

    public MercadoResource(final MercadoService mercadoService) {
        this.mercadoService = mercadoService;
    }

    @GetMapping
    public ResponseEntity<List<MercadoDTO>> getAllMercados() {
        return ResponseEntity.ok(mercadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MercadoDTO> getMercado(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(mercadoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createMercado(@RequestBody @Valid final MercadoDTO mercadoDTO) {
        return new ResponseEntity<>(mercadoService.create(mercadoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMercado(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MercadoDTO mercadoDTO) {
        mercadoService.update(id, mercadoDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMercado(@PathVariable(name = "id") final Long id) {
        mercadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
