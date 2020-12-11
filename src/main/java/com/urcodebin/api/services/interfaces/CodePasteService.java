package com.urcodebin.api.services.interfaces;

import com.urcodebin.api.entities.CodePaste;

import java.util.Optional;
import java.util.UUID;

public interface CodePasteService {

    Optional<CodePaste> findByCodePasteId(UUID id);
}
