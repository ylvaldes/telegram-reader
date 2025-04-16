package com.ylvaldes.telegram_reader.service;

import com.ylvaldes.telegram_reader.domain.Mercado;
import com.ylvaldes.telegram_reader.model.MercadoDTO;
import com.ylvaldes.telegram_reader.repos.MercadoRepository;
import com.ylvaldes.telegram_reader.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author YasmaniLedesmaValdez
 * @project telegram-reader
 * @package com.ylvaldes.telegram_reader.service
 * @created 22/4/2023
 * @implNote
 */
@Service
public class MercadoService {

    private final MercadoRepository mercadoRepository;

    public MercadoService(final MercadoRepository mercadoRepository) {
        this.mercadoRepository = mercadoRepository;
    }

    public List<MercadoDTO> findAll() {
        final List<Mercado> mercados = mercadoRepository.findAll(Sort.by("id"));
        return mercados.stream()
                .map((mercado) -> mapToDTO(mercado, new MercadoDTO()))
                .toList();
    }

    public MercadoDTO get(final Long id) {
        return mercadoRepository.findById(id)
                .map(mercado -> mapToDTO(mercado, new MercadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MercadoDTO mercadoDTO) {
        final Mercado mercado = new Mercado();
        mapToEntity(mercadoDTO, mercado);
        return mercadoRepository.save(mercado).getId();
    }

    public void update(final Long id, final MercadoDTO mercadoDTO) {
        final Mercado mercado = mercadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(mercadoDTO, mercado);
        mercadoRepository.save(mercado);
    }

    public void delete(final Long id) {
        mercadoRepository.deleteById(id);
    }

    private MercadoDTO mapToDTO(final Mercado mercado, final MercadoDTO mercadoDTO) {
        mercadoDTO.setId(mercado.getId());
        mercadoDTO.setNombre(mercado.getNombre());
        mercadoDTO.setRut(mercado.getRut());
        mercadoDTO.setDescripcion(mercado.getDescripcion());
        mercadoDTO.setUrlGetTiket(mercado.getUrlGetTiket());
        return mercadoDTO;
    }

    private Mercado mapToEntity(final MercadoDTO mercadoDTO, final Mercado mercado) {
        mercado.setNombre(mercadoDTO.getNombre());
        mercado.setRut(mercadoDTO.getRut());
        mercado.setDescripcion(mercadoDTO.getDescripcion());
        mercado.setUrlGetTiket(mercadoDTO.getUrlGetTiket());
        return mercado;
    }

    public boolean nombreExists(final String nombre) {
        return mercadoRepository.existsByNombreIgnoreCase(nombre);
    }

    public boolean rutExists(final Long rut) {
        return mercadoRepository.existsByRut(rut);
    }

    public MercadoDTO getByRut(final Long rut) {
        return mercadoRepository.findByRut(rut)
                .map(mercado -> mapToDTO(mercado, new MercadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

}
