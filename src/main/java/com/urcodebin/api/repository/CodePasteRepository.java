package com.urcodebin.api.repository;

import com.urcodebin.api.entities.CodePaste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CodePasteRepository extends JpaRepository<CodePaste, UUID> {
}
