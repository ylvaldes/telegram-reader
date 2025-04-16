package com.ylvaldes.telegram_reader.service;

import com.ylvaldes.telegram_reader.domain.UrlData;
import com.ylvaldes.telegram_reader.model.UrlDataDTO;
import com.ylvaldes.telegram_reader.repos.UrlDataRepository;
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
public class UrlDataService {

    private final UrlDataRepository urlDataRepository;

    public UrlDataService(final UrlDataRepository urlDataRepository) {
        this.urlDataRepository = urlDataRepository;
    }

    public List<UrlDataDTO> findAll() {
        final List<UrlData> urlDatas = urlDataRepository.findAll(Sort.by("id"));
        return urlDatas.stream()
                .map((urlData) -> mapToDTO(urlData, new UrlDataDTO()))
                .toList();
    }

    public UrlDataDTO get(final Long id) {
        return urlDataRepository.findById(id)
                .map(urlData -> mapToDTO(urlData, new UrlDataDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UrlDataDTO urlDataDTO) {
        final UrlData urlData = new UrlData();
        mapToEntity(urlDataDTO, urlData);
        return urlDataRepository.save(urlData).getId();
    }

    public void update(final Long id, final UrlDataDTO urlDataDTO) {
        final UrlData urlData = urlDataRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(urlDataDTO, urlData);
        urlDataRepository.save(urlData);
    }

    public void delete(final Long id) {
        urlDataRepository.deleteById(id);
    }

    private UrlDataDTO mapToDTO(final UrlData urlData, final UrlDataDTO urlDataDTO) {
        urlDataDTO.setId(urlData.getId());
        urlDataDTO.setTipoCFE(urlData.getTipoCFE());
        urlDataDTO.setSerie(urlData.getSerie());
        urlDataDTO.setNumero(urlData.getNumero());
        urlDataDTO.setFecha(urlData.getFecha());
        urlDataDTO.setCodigoSeguridad(urlData.getCodigoSeguridad());
        urlDataDTO.setMontoTotal(urlData.getMontoTotal());
        return urlDataDTO;
    }

    private UrlData mapToEntity(final UrlDataDTO urlDataDTO, final UrlData urlData) {
        urlData.setTipoCFE(urlDataDTO.getTipoCFE());
        urlData.setSerie(urlDataDTO.getSerie());
        urlData.setNumero(urlDataDTO.getNumero());
        urlData.setFecha(urlDataDTO.getFecha());
        urlData.setCodigoSeguridad(urlDataDTO.getCodigoSeguridad());
        urlData.setMontoTotal(urlDataDTO.getMontoTotal());
        return urlData;
    }

}
