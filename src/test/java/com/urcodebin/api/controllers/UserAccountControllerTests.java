package com.urcodebin.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import com.urcodebin.api.dto.UserAccountDTO;
import com.urcodebin.api.entities.UserAccount;
import com.urcodebin.api.error.exception.UserAccountNotFoundException;
import com.urcodebin.api.services.interfaces.UserAccountService;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserAccountController.class)
public class UserAccountControllerTests {

    private MockMvc mockMvc;

    @MockBean
    @Qualifier("AccountService")
    public UserAccountService userAccountService;

    @Autowired
    public WebApplicationContext webApplicationContext;

    private final ModelMapper modelMapper = new ModelMapper();

    UserAccount fakeAccount = new UserAccount();
    UserAccountDTO fakeAccountDTO;

    private final static String GET_ACCOUNT_FROM_ID_PATH = "/api/account/{accountId}";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        fakeAccount.setId(100L);
        fakeAccount.setUsername("Fake Account");
        fakeAccount.setEmail("fakeEmail@gmail.com");
        fakeAccountDTO = convertToDTO(fakeAccount);

    }

    private UserAccountDTO convertToDTO(UserAccount userAccount) {
        return modelMapper.map(userAccount, UserAccountDTO.class);
    }

    @Test
    public void getUserAccountByIdWithCorrectIdReturnsRightAccount() throws Exception {
        when(userAccountService.getUserAccountById(fakeAccount.getId())).thenReturn(fakeAccount);

        mockMvc.perform(get(GET_ACCOUNT_FROM_ID_PATH, fakeAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".*", is(convertToListFormat(fakeAccountDTO))));

        verify(userAccountService, times(1)).getUserAccountById(any());
    }

    @Test
    public void getUserAccountByIdWithWrongIdReturnsHttpNotFound() throws Exception {
        when(userAccountService.getUserAccountById(any())).thenThrow(UserAccountNotFoundException.class);

        mockMvc.perform(get(GET_ACCOUNT_FROM_ID_PATH, 1001))
                .andExpect(status().isNotFound());

        verify(userAccountService, times(1)).getUserAccountById(any());
    }

    private List<Object> convertToListFormat(UserAccountDTO codePastes) {
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
