package com.urcodebin.api.services;

import com.urcodebin.api.entities.CodePaste;
import com.urcodebin.api.error.exception.PasteNotFoundException;
import com.urcodebin.api.repository.CodePasteRepository;
import com.urcodebin.api.services.interfaces.CodePasteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("PasteService")
public class CodePasteServiceImpl implements CodePasteService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final CodePasteRepository codePasteRepository;

    @Autowired
    public CodePasteServiceImpl(CodePasteRepository codePasteRepository) {
        this.codePasteRepository = codePasteRepository;
    }

    @Override
    public Optional<CodePaste> findByCodePasteId(UUID id) {
        final Optional<CodePaste> foundCodePaste = codePasteRepository.findById(id);
        if(!foundCodePaste.isPresent())
            LOGGER.warn("No CodePaste was found using ID: {}", id);
        return foundCodePaste;
    }
}
