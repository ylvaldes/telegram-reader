package com.ylvaldes.telegram_reader.service;

import com.ylvaldes.telegram_reader.domain.Mercado;
import com.ylvaldes.telegram_reader.domain.Registro;
import com.ylvaldes.telegram_reader.domain.Url;
import com.ylvaldes.telegram_reader.model.RegistroDTO;
import com.ylvaldes.telegram_reader.repos.MercadoRepository;
import com.ylvaldes.telegram_reader.repos.RegistroRepository;
import com.ylvaldes.telegram_reader.repos.UrlRepository;
import com.ylvaldes.telegram_reader.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final UrlRepository urlRepository;
    private final MercadoRepository mercadoRepository;

    public RegistroService(final RegistroRepository registroRepository,
            final UrlRepository urlRepository, final MercadoRepository mercadoRepository) {
        this.registroRepository = registroRepository;
        this.urlRepository = urlRepository;
        this.mercadoRepository = mercadoRepository;
    }

    public List<RegistroDTO> findAll() {
        final List<Registro> registros = registroRepository.findAll(Sort.by("id"));
        return registros.stream()
                .map((registro) -> mapToDTO(registro, new RegistroDTO()))
                .toList();
    }

    public RegistroDTO get(final Long id) {
        return registroRepository.findById(id)
                .map(registro -> mapToDTO(registro, new RegistroDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RegistroDTO registroDTO) {
        final Registro registro = new Registro();
        mapToEntity(registroDTO, registro);
        return registroRepository.save(registro).getId();
    }

    public void update(final Long id, final RegistroDTO registroDTO) {
        final Registro registro = registroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(registroDTO, registro);
        registroRepository.save(registro);
    }

    public void delete(final Long id) {
        registroRepository.deleteById(id);
    }

    private RegistroDTO mapToDTO(final Registro registro, final RegistroDTO registroDTO) {
        registroDTO.setId(registro.getId());
        registroDTO.setImporte(registro.getImporte());
        registroDTO.setMoneda(registro.getMoneda());
        registroDTO.setCategoria(registro.getCategoria());
        registroDTO.setFecha(registro.getFecha());
        registroDTO.setDescripcion(registro.getDescripcion());
        registroDTO.setBeneficiario(registro.getBeneficiario());
        registroDTO.setDireccion(registro.getDireccion());
        registroDTO.setIdUrl(registro.getIdUrl() == null ? null : registro.getIdUrl().getId());
        registroDTO.setIdMercado(registro.getIdMercado() == null ? null : registro.getIdMercado().getId());
        return registroDTO;
    }

    private Registro mapToEntity(final RegistroDTO registroDTO, final Registro registro) {
        registro.setImporte(registroDTO.getImporte());
        registro.setMoneda(registroDTO.getMoneda());
        registro.setCategoria(registroDTO.getCategoria());
        registro.setFecha(registroDTO.getFecha());
        registro.setDescripcion(registroDTO.getDescripcion());
        registro.setBeneficiario(registroDTO.getBeneficiario());
        registro.setDireccion(registroDTO.getDireccion());
        final Url idUrl = registroDTO.getIdUrl() == null ? null : urlRepository.findById(registroDTO.getIdUrl())
                .orElseThrow(() -> new NotFoundException("idUrl not found"));
        registro.setIdUrl(idUrl);
        final Mercado idMercado = registroDTO.getIdMercado() == null ? null : mercadoRepository.findById(registroDTO.getIdMercado())
                .orElseThrow(() -> new NotFoundException("idMercado not found"));
        registro.setIdMercado(idMercado);
        return registro;
    }

}
