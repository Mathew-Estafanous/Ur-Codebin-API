package com.urcodebin.api.controllers;

import com.urcodebin.api.controllers.requestbody.SignupRequestBody;
import com.urcodebin.api.dto.UserAccountDTO;
import com.urcodebin.api.entities.UserAccount;
import com.urcodebin.api.error.exception.AccountInformationTakenException;
import com.urcodebin.api.error.exception.UserAccountNotFoundException;
import com.urcodebin.api.services.interfaces.UserAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

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
        Optional<UserAccount> foundUserAccount = userAccountService.getUserAccountById(accountId);
        if(!foundUserAccount.isPresent())
            throw new UserAccountNotFoundException("User Account with given id was not found.");
        return convertToDTO(foundUserAccount.get());
    }

    @PostMapping("/signup")
    public UserAccountDTO signupForNewAccount(@RequestBody SignupRequestBody signupAccount) {
        boolean usernameIsTaken = userAccountService.isAccountUsernameTaken(signupAccount.getUsername());
        if(usernameIsTaken)
            throw new AccountInformationTakenException("Username provided is already in use. Please use another username.");
        boolean emailIsTaken = userAccountService.isAccountEmailTaken(signupAccount.getEmail());
        if(emailIsTaken)
            throw new AccountInformationTakenException("Email provided is already in use. Please use another email.");
        final UserAccount signedUpAccount = userAccountService.signupNewUserAccount(signupAccount);
        return convertToDTO(signedUpAccount);
    }

    private UserAccountDTO convertToDTO(UserAccount userAccount) {
        return modelMapper.map(userAccount, UserAccountDTO.class);
    }
}
