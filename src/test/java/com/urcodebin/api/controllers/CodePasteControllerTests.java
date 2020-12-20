package com.urcodebin.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import com.urcodebin.api.entities.CodePaste;
import com.urcodebin.api.enums.PasteSyntax;
import com.urcodebin.api.enums.PasteVisibility;
import com.urcodebin.api.services.interfaces.CodePasteService;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(CodePasteController.class)
public class CodePasteControllerTests {

    private MockMvc mockMvc;

    @MockBean
    @Qualifier("PasteService")
    public CodePasteService codePasteService;

    @Autowired
    public WebApplicationContext webApplicationContext;

    CodePaste firstPaste = new CodePaste();
    CodePaste secondPaste = new CodePaste();

    private final String PUBLIC_PASTE_PATH = "/api/paste/public";
    private final String PASTE_FROM_ID_PATH = "/api/paste/{pasteId}";
    private final String PASTE_TITLE = "paste_title";
    private final String PASTE_SYNTAX = "paste_syntax";
    private final String LIMIT = "limit";

    @Before
    public void setup() {
        Mockito.reset(codePasteService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        firstPaste.setPasteVisibility(PasteVisibility.PUBLIC);
        firstPaste.setPasteSyntax(PasteSyntax.JAVA);
        firstPaste.setPasteTitle("My Fake Paste");
        firstPaste.setSourceCode("System.out.println('this is code);");
        firstPaste.setPasteExpirationDate(LocalDateTime.now());

        secondPaste.setPasteVisibility(PasteVisibility.PUBLIC);
        secondPaste.setPasteSyntax(PasteSyntax.JAVA);
        secondPaste.setPasteTitle("My Java Program");
        secondPaste.setSourceCode("Object myObj = new Object();");
        secondPaste.setPasteExpirationDate(LocalDateTime.now());
    }

    @Test
    public void getPasteFromIdWithCorrectIdReturnsFoundCodePaste() throws Exception {
        when(codePasteService.findByCodePasteId(firstPaste.getPasteId())).thenReturn(Optional.of(firstPaste));

        mockMvc.perform(get(PASTE_FROM_ID_PATH, firstPaste.getPasteId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".*", is(convertPasteToList(firstPaste))));

        verify(codePasteService, times(1)).findByCodePasteId(any(UUID.class));
    }

    @Test
    public void getPasteFromIdWithWrongIdResultsInHttpNotFound() throws Exception {
        when(codePasteService.findByCodePasteId(any(UUID.class))).thenReturn(Optional.empty());

        mockMvc.perform(get(PASTE_FROM_ID_PATH, UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());

        verify(codePasteService, times(1)).findByCodePasteId(any(UUID.class));
    }

    @Test
    public void getPasteIdWithInvalidIdFormatResultsInHttpBadRequest() throws Exception {
        mockMvc.perform(get(PASTE_FROM_ID_PATH, "Invalid UUID Format"))
                .andExpect(status().isBadRequest());

        verify(codePasteService, times(0)).findByCodePasteId(any(UUID.class));
    }

    @Test
    public void getPublicPastesWithAllValidParametersFilledReturnsListOfFoundPastes() throws Exception {
        when(codePasteService.findListOfCodePastesBy("My", PasteSyntax.JAVA, 2))
                .thenReturn(Arrays.asList(firstPaste, secondPaste));

        final MockHttpServletRequestBuilder request =  get(PUBLIC_PASTE_PATH)
                .param(PASTE_TITLE, "My")
                .param(PASTE_SYNTAX, "JAVA")
                .param(LIMIT, "2");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].*", is(convertPasteToList(firstPaste))))
                .andExpect(jsonPath("$[1].*", is(convertPasteToList(secondPaste))));

        verify(codePasteService, times(0)).findListOfCodePastesBy(anyString(), anyInt());
    }

    @Test
    public void getPublicPastesWithWrongPasteTitleReturnsNoFoundPastes() throws Exception {
        when(codePasteService.findListOfCodePastesBy("Wrong", PasteSyntax.JAVA, 2))
                .thenReturn(Collections.emptyList());

        final MockHttpServletRequestBuilder request = get(PUBLIC_PASTE_PATH)
                .param(PASTE_TITLE, "Wrong")
                .param(PASTE_SYNTAX, "JAVA")
                .param(LIMIT, "2");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath(".*", is(Collections.emptyList())));

        verify(codePasteService, times(0)).findListOfCodePastesBy(anyString(), anyInt());
    }

    @Test
    public void getPublicPastesWithMissingPasteTitleParameterReturnsListOfFoundPastes() throws Exception {
        when(codePasteService.findListOfCodePastesBy("", PasteSyntax.JAVA, 2))
                .thenReturn(Arrays.asList(firstPaste, secondPaste));

        final MockHttpServletRequestBuilder request = get(PUBLIC_PASTE_PATH)
                .param(PASTE_SYNTAX, "JAVA")
                .param(LIMIT, "2");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].*", is(convertPasteToList(firstPaste))))
                .andExpect(jsonPath("$[1].*", is(convertPasteToList(secondPaste))));

        verify(codePasteService, times(0)).findListOfCodePastesBy(anyString(), anyInt());
    }

    @Test
    public void getPublicPasteWithMissingPasteSyntaxParameterReturnsListOfFoundPastes() throws Exception {
        when(codePasteService.findListOfCodePastesBy("My", 2))
                .thenReturn(Arrays.asList(firstPaste, secondPaste));

        final MockHttpServletRequestBuilder request = get(PUBLIC_PASTE_PATH)
                .param(PASTE_TITLE, "My")
                .param(LIMIT, "2");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].*", is(convertPasteToList(firstPaste))))
                .andExpect(jsonPath("$[1].*", is(convertPasteToList(secondPaste))));

        verify(codePasteService, times(0))
                .findListOfCodePastesBy(anyString(), any(PasteSyntax.class), anyInt());
    }

    @Test
    public void getPublicPasteWithInvalidPasteSyntaxParameterResultsInHttpBadRequest() throws Exception {
        final MockHttpServletRequestBuilder request = get(PUBLIC_PASTE_PATH)
                .param(PASTE_TITLE, "My")
                .param(PASTE_SYNTAX, "Invalid Input")
                .param(LIMIT, "2");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

        verify(codePasteService, times(0))
                .findListOfCodePastesBy(anyString(), any(PasteSyntax.class), anyInt());
        verify(codePasteService, times(0))
                .findListOfCodePastesBy(anyString(), anyInt());
    }

    @Test
    public void getPublicPasteWithMissingLimitParameterReturnsListOfFoundPastes() throws Exception {
        when(codePasteService.findListOfCodePastesBy("My", PasteSyntax.JAVA, 5))
                .thenReturn(Arrays.asList(firstPaste, secondPaste));

        final MockHttpServletRequestBuilder request = get(PUBLIC_PASTE_PATH)
                .param(PASTE_TITLE, "My")
                .param(PASTE_SYNTAX, "JAVA");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].*", is(convertPasteToList(firstPaste))))
                .andExpect(jsonPath("$[1].*", is(convertPasteToList(secondPaste))));

        verify(codePasteService, times(0))
                .findListOfCodePastesBy(anyString(), anyInt());
    }

    @Test
    public void getPublicPasteWithOutOfBoundsLimitParameterResultsInHttpBadRequest() throws Exception {
        final MockHttpServletRequestBuilder belowLimitRequest = get(PUBLIC_PASTE_PATH)
                .param(PASTE_TITLE, "My")
                .param(PASTE_SYNTAX, "Invalid Input")
                .param(LIMIT, "0");

        final MockHttpServletRequestBuilder moreThanLimitRequest = get(PUBLIC_PASTE_PATH)
                .param(PASTE_TITLE, "My")
                .param(PASTE_SYNTAX, "Invalid Input")
                .param(LIMIT, "21");

        mockMvc.perform(belowLimitRequest)
                .andExpect(status().isBadRequest());
        mockMvc.perform(moreThanLimitRequest)
                .andExpect(status().isBadRequest());

        verify(codePasteService, times(0))
                .findListOfCodePastesBy(anyString(), any(PasteSyntax.class), anyInt());
        verify(codePasteService, times(0))
                .findListOfCodePastesBy(anyString(), anyInt());
    }

    private List<Object> convertPasteToList(CodePaste codePastes) {
        ObjectWriter writer = new ObjectMapper().writer();
        JSONArray jsonArray;
        try {
            String initialJson = writer.writeValueAsString(codePastes);
            jsonArray = new JSONArray(JsonPath.read(initialJson, ".*").toString());
        } catch (JsonProcessingException | JSONException e) {
            return Collections.emptyList();
        }

        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> {
                    try {
                        return jsonArray.get(index);
                    } catch (JSONException e) {
                        return "";
                    }
                })
                .collect(Collectors.toList());
    }
}
