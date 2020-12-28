package com.urcodebin.api.controllers;

import com.urcodebin.api.dto.UserAccountDTO;
import com.urcodebin.api.entities.UserAccount;
import com.urcodebin.api.error.exception.UserAccountNotFoundException;
import com.urcodebin.api.services.interfaces.UserAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class UserAccountController {

    private final UserAccountService userAccountService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserAccountController(@Qualifier("AccountService") UserAccountService userAccountService,
                                 ModelMapper modelMapper) {
        this.userAccountService = userAccountService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{account_id}")
    public UserAccountDTO getUserAccountById(@PathVariable("account_id") Long accountId) {
        final Optional<UserAccount> foundUserAccount = userAccountService.getUserAccountById(accountId);
        if(!foundUserAccount.isPresent())
            throw new UserAccountNotFoundException("User Account with given id was not found.");
        return convertToDTO(foundUserAccount.get());
    }

    private UserAccountDTO convertToDTO(UserAccount userAccount) {
        return modelMapper.map(userAccount, UserAccountDTO.class);
    }
}
