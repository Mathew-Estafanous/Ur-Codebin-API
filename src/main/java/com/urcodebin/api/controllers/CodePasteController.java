package com.urcodebin.api.controllers;

import com.urcodebin.api.entities.CodePaste;
import com.urcodebin.api.enums.PasteSyntax;
import com.urcodebin.api.error.exception.PasteNotFoundException;
import com.urcodebin.api.services.interfaces.CodePasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/paste")
public class CodePasteController {

    private final CodePasteService codePasteService;

    @Autowired
    public CodePasteController(@Qualifier("PasteService") CodePasteService codePasteService) {
        this.codePasteService = codePasteService;
    }

    @GetMapping(path = "/{pasteId}")
    public CodePaste getCodePasteFromId(@PathVariable("pasteId") String pasteId) {
        UUID pasteUUID = createUUIDFromString(pasteId);
        Optional<CodePaste> foundPasteId = codePasteService.findByCodePasteId(pasteUUID);
        return foundPasteId.orElseThrow(() -> new PasteNotFoundException("No CodePaste has been found with the given id."));
    }

    @DeleteMapping(path = "/{pasteId}")
    public void deleteCodePasteWithId(@PathVariable("pasteId") String pasteId) {
        UUID pasteUUID = createUUIDFromString(pasteId);
        if(!codePasteService.doesCodePasteWithIdExist(pasteUUID))
            throw new PasteNotFoundException("No CodePaste has been found with the given id.");

        codePasteService.deleteCodePasteById(pasteUUID);
    }

    @GetMapping(path = "/public")
    public List<CodePaste> getListOfPublicPastesWith(
                @RequestParam(value = "paste_title", defaultValue = "") String pasteTitle,
                @RequestParam(value = "paste_syntax", required = false) String pasteSyntax,
                @RequestParam(value = "limit", defaultValue = "5") int limit) {
        if(limit > 20 || limit < 1)
            throw new IllegalArgumentException("limit number must be between 1 and 20.");

        if(pasteSyntax == null)
            return codePasteService.findListOfCodePastesBy(pasteTitle, limit);

        PasteSyntax pasteSyntaxToSearch = createPasteSyntaxFromString(pasteSyntax);
        return codePasteService.findListOfCodePastesBy(pasteTitle, pasteSyntaxToSearch, limit);
    }

    private UUID createUUIDFromString(String stringId) {
        try {
            return UUID.fromString(stringId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format, please format properly!");
        }
    }

    private PasteSyntax createPasteSyntaxFromString(String pasteSyntax) {
        try {
            return PasteSyntax.valueOf(pasteSyntax);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("paste_syntax parameter is not any of the given options.");
        }
    }
}
