package com.urcodebin.api.services;

import com.urcodebin.api.entities.UserAccount;
import com.urcodebin.api.repository.UserAccountRepository;
import com.urcodebin.api.services.interfaces.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("AccountService")
public class UserAccountServiceImpl implements UserAccountService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Optional<UserAccount> getUserAccountById(Long accountId) {
        LOGGER.info("Finding UserAccount with ID: {}", accountId);
        final Optional<UserAccount> foundAccount = userAccountRepository.findById(accountId);
        return foundAccount.or(Optional::empty);
    }
}
