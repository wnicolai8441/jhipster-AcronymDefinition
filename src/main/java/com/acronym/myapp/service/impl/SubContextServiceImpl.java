package com.acronym.myapp.service.impl;

import com.acronym.myapp.domain.SubContext;
import com.acronym.myapp.repository.SubContextRepository;
import com.acronym.myapp.service.SubContextService;
import com.acronym.myapp.service.dto.SubContextDTO;
import com.acronym.myapp.service.mapper.SubContextMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.acronym.myapp.domain.SubContext}.
 */
@Service
@Transactional
public class SubContextServiceImpl implements SubContextService {

    private final Logger log = LoggerFactory.getLogger(SubContextServiceImpl.class);

    private final SubContextRepository subContextRepository;

    private final SubContextMapper subContextMapper;

    public SubContextServiceImpl(SubContextRepository subContextRepository, SubContextMapper subContextMapper) {
        this.subContextRepository = subContextRepository;
        this.subContextMapper = subContextMapper;
    }

    @Override
    public SubContextDTO save(SubContextDTO subContextDTO) {
        log.debug("Request to save SubContext : {}", subContextDTO);
        SubContext subContext = subContextMapper.toEntity(subContextDTO);
        subContext = subContextRepository.save(subContext);
        return subContextMapper.toDto(subContext);
    }

    @Override
    public SubContextDTO update(SubContextDTO subContextDTO) {
        log.debug("Request to update SubContext : {}", subContextDTO);
        SubContext subContext = subContextMapper.toEntity(subContextDTO);
        subContext = subContextRepository.save(subContext);
        return subContextMapper.toDto(subContext);
    }

    @Override
    public Optional<SubContextDTO> partialUpdate(SubContextDTO subContextDTO) {
        log.debug("Request to partially update SubContext : {}", subContextDTO);

        return subContextRepository
            .findById(subContextDTO.getId())
            .map(existingSubContext -> {
                subContextMapper.partialUpdate(existingSubContext, subContextDTO);

                return existingSubContext;
            })
            .map(subContextRepository::save)
            .map(subContextMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubContextDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubContexts");
        return subContextRepository.findAll(pageable).map(subContextMapper::toDto);
    }

    public Page<SubContextDTO> findAllWithEagerRelationships(Pageable pageable) {
        return subContextRepository.findAllWithEagerRelationships(pageable).map(subContextMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubContextDTO> findOne(Long id) {
        log.debug("Request to get SubContext : {}", id);
        return subContextRepository.findOneWithEagerRelationships(id).map(subContextMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubContext : {}", id);
        subContextRepository.deleteById(id);
    }
}
