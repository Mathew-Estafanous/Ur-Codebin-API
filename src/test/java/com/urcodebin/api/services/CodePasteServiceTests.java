package com.urcodebin.api.services;

import com.urcodebin.api.entities.CodePaste;
import com.urcodebin.api.enums.PasteSyntax;
import com.urcodebin.api.enums.PasteVisibility;
import com.urcodebin.api.repository.CodePasteRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CodePasteServiceTests {

    @Mock
    private CodePasteRepository codePasteRepository;

    @InjectMocks
    private CodePasteServiceImpl codePasteService;

    CodePaste fakeCodePaste;

    @Before
    public void setup() {
        fakeCodePaste = new CodePaste();
        fakeCodePaste.setPasteTitle("Fake Title");
        fakeCodePaste.setPasteSyntax(PasteSyntax.NONE);
        fakeCodePaste.setPasteVisibility(PasteVisibility.PUBLIC);
    }

    @Test
    public void findCodePasteWithCorrectIdReturnsCorrectPaste() {
        when(codePasteRepository.findById(any(UUID.class))).thenReturn(Optional.of(fakeCodePaste));

        final Optional<CodePaste> foundCodePaste = codePasteService.findByCodePasteId(fakeCodePaste.getPasteId());
        Assert.assertTrue(foundCodePaste.isPresent());
        Assert.assertEquals(fakeCodePaste, foundCodePaste.get());
    }

    @Test
    public void findCodePasteWithWrongIdReturnsNoResults() {
        when(codePasteRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        final Optional<CodePaste> foundCodePaste = codePasteService.findByCodePasteId(UUID.randomUUID());
        Assert.assertFalse(foundCodePaste.isPresent());
    }

    @Test
    public void findListOfCodePasteByCorrectTitleReturnsCorrectPastes() {
        when(codePasteRepository.findCodePastesContainTitle(anyString(), any(Pageable.class)))
                .thenReturn(Collections.singletonList(fakeCodePaste));

        final List<CodePaste> listOfCodePastes = codePasteService.findListOfCodePastesBy(fakeCodePaste.getPasteTitle(), 1);
        Assert.assertEquals(listOfCodePastes, Collections.singletonList(fakeCodePaste));
    }

    @Test
    public void findListOfCodePasteWithWrongTitleReturnsEmptyList() {
        when(codePasteRepository.findCodePastesContainTitle(anyString(), any(Pageable.class)))
                .thenReturn(List.of());

        final List<CodePaste> listOfCodePastes = codePasteService.findListOfCodePastesBy("Wrong Title", 1);
        Assert.assertTrue(listOfCodePastes.isEmpty());
    }

    @Test
    public void findListOfCodePastesWithCorrectTitleAndPasteSyntaxReturnsCorrectPastes() {
        when(codePasteRepository.findCodePastesContaining(anyString(), any(PasteSyntax.class), any(Pageable.class)))
                .thenReturn(Collections.singletonList(fakeCodePaste));

        final List<CodePaste> listOfCodePastes = codePasteService.findListOfCodePastesBy(
                fakeCodePaste.getPasteTitle(), fakeCodePaste.getPasteSyntax(), 1);
        Assert.assertEquals(listOfCodePastes, Collections.singletonList(fakeCodePaste));
    }

    @Test
    public void findListOfCodePastesWithWrongTitleAndCorrectPasteSyntaxReturnsEmptyList() {
        when(codePasteRepository.findCodePastesContaining(anyString(), any(PasteSyntax.class), any(Pageable.class)))
                .thenReturn(List.of());

        final List<CodePaste> listOfCodePastes = codePasteService.findListOfCodePastesBy(
                "Wrong Title", fakeCodePaste.getPasteSyntax(), 1);
        Assert.assertTrue(listOfCodePastes.isEmpty());
    }

    @Test
    public void findListOfCodePastesWithCorrectTitleAndWrongPasteSyntaxReturnsEmptyList() {
        when(codePasteRepository.findCodePastesContaining(anyString(), any(PasteSyntax.class), any(Pageable.class)))
                .thenReturn(List.of());

        final List<CodePaste> listOfCodePastes = codePasteService.findListOfCodePastesBy(
                    fakeCodePaste.getPasteTitle(), PasteSyntax.CLANG, 1);
        Assert.assertTrue(listOfCodePastes.isEmpty());
    }
}
