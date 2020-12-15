package com.urcodebin.api.services;

import com.urcodebin.api.entities.CodePaste;
import com.urcodebin.api.enums.PasteSyntax;
import com.urcodebin.api.enums.PasteVisibility;
import com.urcodebin.api.repository.CodePasteRepository;
import com.urcodebin.api.services.interfaces.CodePasteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<CodePaste> findListOfCodePastesBy(String pasteTitle, PasteSyntax pasteSyntax, int limit) {
        Pageable pageLimit = PageRequest.of(0, limit);
        return codePasteRepository.findCodePastesContaining(pasteTitle, pasteSyntax, pageLimit);
    }

    @Override
    public List<CodePaste> findListOfCodePastesBy(String pasteTitle, int limit) {
        Pageable pageLimit = PageRequest.of(0, limit);
        return codePasteRepository.findCodePastesContainTitle(pasteTitle, pageLimit);
    }
}
