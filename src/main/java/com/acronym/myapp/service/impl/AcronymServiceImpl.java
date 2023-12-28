package com.acronym.myapp.service.impl;

import com.acronym.myapp.domain.Acronym;
import com.acronym.myapp.repository.AcronymRepository;
import com.acronym.myapp.service.AcronymService;
import com.acronym.myapp.service.dto.AcronymDTO;
import com.acronym.myapp.service.mapper.AcronymMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.acronym.myapp.domain.Acronym}.
 */
@Service
@Transactional
public class AcronymServiceImpl implements AcronymService {

    private final Logger log = LoggerFactory.getLogger(AcronymServiceImpl.class);

    private final AcronymRepository acronymRepository;

    private final AcronymMapper acronymMapper;

    public AcronymServiceImpl(AcronymRepository acronymRepository, AcronymMapper acronymMapper) {
        this.acronymRepository = acronymRepository;
        this.acronymMapper = acronymMapper;
    }

    @Override
    public AcronymDTO save(AcronymDTO acronymDTO) {
        log.debug("Request to save Acronym : {}", acronymDTO);
        Acronym acronym = acronymMapper.toEntity(acronymDTO);
        acronym = acronymRepository.save(acronym);
        return acronymMapper.toDto(acronym);
    }

    @Override
    public AcronymDTO update(AcronymDTO acronymDTO) {
        log.debug("Request to update Acronym : {}", acronymDTO);
        Acronym acronym = acronymMapper.toEntity(acronymDTO);
        acronym = acronymRepository.save(acronym);
        return acronymMapper.toDto(acronym);
    }

    @Override
    public Optional<AcronymDTO> partialUpdate(AcronymDTO acronymDTO) {
        log.debug("Request to partially update Acronym : {}", acronymDTO);

        return acronymRepository
            .findById(acronymDTO.getId())
            .map(existingAcronym -> {
                acronymMapper.partialUpdate(existingAcronym, acronymDTO);

                return existingAcronym;
            })
            .map(acronymRepository::save)
            .map(acronymMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcronymDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Acronyms");
        return acronymRepository.findAll(pageable).map(acronymMapper::toDto);
    }

    public Page<AcronymDTO> findAllWithEagerRelationships(Pageable pageable) {
        return acronymRepository.findAllWithEagerRelationships(pageable).map(acronymMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcronymDTO> findOne(Long id) {
        log.debug("Request to get Acronym : {}", id);
        return acronymRepository.findOneWithEagerRelationships(id).map(acronymMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Acronym : {}", id);
        acronymRepository.deleteById(id);
    }
}
