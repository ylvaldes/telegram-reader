package com.ylvaldes.telegram_reader.service;

import com.ylvaldes.telegram_reader.domain.Mercado;
import com.ylvaldes.telegram_reader.domain.Url;
import com.ylvaldes.telegram_reader.domain.UrlData;
import com.ylvaldes.telegram_reader.model.PendingDataResponse;
import com.ylvaldes.telegram_reader.model.Status;
import com.ylvaldes.telegram_reader.model.UrlDTO;
import com.ylvaldes.telegram_reader.model.UrlDTOFull;
import com.ylvaldes.telegram_reader.repos.MercadoRepository;
import com.ylvaldes.telegram_reader.repos.UrlDataRepository;
import com.ylvaldes.telegram_reader.repos.UrlRepository;
import com.ylvaldes.telegram_reader.util.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UrlService {
    @Autowired
    MercadoService mercadoService;
    @Autowired
    UrlDataService urlDataService;

    private final UrlRepository urlRepository;
    private final MercadoRepository mercadoRepository;
    private final UrlDataRepository urlDataRepository;

    public UrlService(final UrlRepository urlRepository, final MercadoRepository mercadoRepository,
                      final UrlDataRepository urlDataRepository) {
        this.urlRepository = urlRepository;
        this.mercadoRepository = mercadoRepository;
        this.urlDataRepository = urlDataRepository;
    }

    public List<UrlDTOFull> findAll() {
        final List<Url> urls = urlRepository.findAll(Sort.by("id"));
        return urls.stream()
                .map((url) -> mapToDTOFull(url, new UrlDTOFull()))
                .toList();
    }

    public List<UrlDTOFull> findAllNew() {
        final List<Url> urls = urlRepository.findAllByStatus(Status.NEW);
        return urls.stream()
                .map((url) -> mapToDTOFull(url, new UrlDTOFull()))
                .toList();
    }

    public List<PendingDataResponse> findPendingData() {
        List<PendingDataResponse> resp=new ArrayList<>();
        PendingDataResponse pendingDataResponse;
        List<UrlDTOFull> list = findAllNew();
        for (UrlDTOFull url : list
        ) {
            pendingDataResponse=new PendingDataResponse();
            pendingDataResponse.setRut(url.getMercado().getRut());
            pendingDataResponse.setTipoCFE(url.getUrlData().getTipoCFE());
            pendingDataResponse.setSerie(url.getUrlData().getSerie());
            pendingDataResponse.setNumero(url.getUrlData().getNumero());
            pendingDataResponse.setMontoTotal(url.getUrlData().getMontoTotal());
            pendingDataResponse.setFecha(url.getUrlData().getFecha());
            pendingDataResponse.setCodigoSeguridad(url.getUrlData().getCodigoSeguridad());
            pendingDataResponse.setNombre(url.getMercado().getNombre());
            pendingDataResponse.setStatusUrl(url.getStatus());
            pendingDataResponse.setUrlGetTiket(url.getMercado().getUrlGetTiket());
            pendingDataResponse.setIdUrl(url.getId());
            resp.add(pendingDataResponse);
        }
        return resp;
    }

    public UrlDTOFull get(final Long id) {
        return urlRepository.findById(id)
                .map(url -> mapToDTOFull(url, new UrlDTOFull()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UrlDTO urlDTO) {
        final Url url = new Url();
        mapToEntity(urlDTO, url);
        return urlRepository.save(url).getId();
    }

    public void update(final Long id, final UrlDTO urlDTO) {
        final Url url = urlRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(urlDTO, url);
        urlRepository.save(url);
    }
    public void changeStatus(final Long id, Status status) {
        final Url url = urlRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        url.setStatus(status);
        urlRepository.save(url);
    }

    public void delete(final Long id) {
        urlRepository.deleteById(id);
    }

    private UrlDTO mapToDTO(final Url url, final UrlDTO urlDTO) {
        urlDTO.setId(url.getId());
        urlDTO.setUrl(url.getUrl());
        urlDTO.setStatus(url.getStatus());
        urlDTO.setIdMercado(url.getIdMercado() == null ? null : url.getIdMercado().getId());
        urlDTO.setIdUrlData(url.getIdUrlData() == null ? null : url.getIdUrlData().getId());
        return urlDTO;
    }

    private UrlDTOFull mapToDTOFull(final Url url, final UrlDTOFull urlDTO) {
        urlDTO.setId(url.getId());
        urlDTO.setUrl(url.getUrl());
        urlDTO.setStatus(url.getStatus());
        urlDTO.setMercado(url.getIdMercado() == null ? null : mercadoService.get(url.getIdMercado().getId()));
        urlDTO.setUrlData(url.getIdUrlData() == null ? null : urlDataService.get(url.getIdUrlData().getId()));
        return urlDTO;
    }

    private Url mapToEntity(final UrlDTO urlDTO, final Url url) {
        url.setUrl(urlDTO.getUrl());
        url.setStatus(urlDTO.getStatus());
        final Mercado idMercado = urlDTO.getIdMercado() == null ? null : mercadoRepository.findById(urlDTO.getIdMercado())
                .orElseThrow(() -> new NotFoundException("idMercado not found"));
        url.setIdMercado(idMercado);
        final UrlData idUrlData = urlDTO.getIdUrlData() == null ? null : urlDataRepository.findById(urlDTO.getIdUrlData())
                .orElseThrow(() -> new NotFoundException("idUrlData not found"));
        url.setIdUrlData(idUrlData);
        return url;
    }

    public boolean urlExists(final String url) {
        return urlRepository.existsByUrlIgnoreCase(url);
    }

}
