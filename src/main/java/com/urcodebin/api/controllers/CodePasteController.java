package com.urcodebin.api.controllers;

import com.urcodebin.api.entities.CodePaste;
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
        UUID pasteUUID;
        try {
            pasteUUID = UUID.fromString(pasteId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format, please format properly!");
        }
        Optional<CodePaste> foundPasteId = codePasteService.findByCodePasteId(pasteUUID);
        return foundPasteId.orElseThrow(() -> new PasteNotFoundException("No CodePaste has been found with the given id."));
    }

    @GetMapping(path = "/public")
    public List<CodePaste> getListOfPublicPastesWith(@RequestParam("paste_title") String pasteTitle,
                                                     @RequestParam("paste_syntax") String pasteSyntax,
                                                     @RequestParam("limit") int limit) {
        return null;
    }
}
