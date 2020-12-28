package com.urcodebin.api.services.interfaces;

import com.urcodebin.api.entities.UserAccount;

import java.util.Optional;

public interface UserAccountService {

    Optional<UserAccount> getUserAccountById(Long accountId);
}
