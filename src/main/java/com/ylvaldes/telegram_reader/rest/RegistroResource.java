package com.ylvaldes.telegram_reader.rest;

import com.ylvaldes.telegram_reader.model.RegistroDTO;
import com.ylvaldes.telegram_reader.service.RegistroService;
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
@RequestMapping(value = "/api/registros", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistroResource {

    private final RegistroService registroService;

    public RegistroResource(final RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping
    public ResponseEntity<List<RegistroDTO>> getAllRegistros() {
        return ResponseEntity.ok(registroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroDTO> getRegistro(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(registroService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRegistro(@RequestBody @Valid final RegistroDTO registroDTO) {
        final Long createdId = registroService.create(registroDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRegistro(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RegistroDTO registroDTO) {
        registroService.update(id, registroDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRegistro(@PathVariable(name = "id") final Long id) {
        registroService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
