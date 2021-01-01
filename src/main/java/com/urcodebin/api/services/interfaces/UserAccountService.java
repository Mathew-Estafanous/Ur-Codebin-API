package com.urcodebin.api.services.interfaces;

import com.urcodebin.api.controllers.requestbody.SignupRequestBody;
import com.urcodebin.api.entities.UserAccount;

public interface UserAccountService {

    UserAccount getUserAccountById(Long accountId);

    UserAccount signupNewUserAccount(SignupRequestBody signupAccount);

    boolean isAccountEmailTaken(String email);

    boolean isAccountUsernameTaken(String username);
}
