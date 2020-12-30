package com.urcodebin.api.services;

import com.urcodebin.api.entities.UserAccount;
import com.urcodebin.api.repository.UserAccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceTests {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    UserAccount testAccount;

    @Before
    public void setup() {
        testAccount = new UserAccount();
        testAccount.setUsername("Test Username");
        testAccount.setEmail("testemail@gmail.com");
        testAccount.setId(105L);
    }

    @Test
    public void getUserAccountByIdWithCorrectIdReturnsRightUserAccount() {
        when(userAccountRepository.findById(testAccount.getId())).thenReturn(Optional.of(testAccount));

        final Optional<UserAccount> foundUserAccount = userAccountService.getUserAccountById(testAccount.getId());
        Assert.assertTrue(foundUserAccount.isPresent());
        Assert.assertEquals(foundUserAccount.get(), testAccount);
    }

    @Test
    public void getUserAccountByIdWithWrongIdReturnsEmptyResult() {
        when(userAccountRepository.findById(100L)).thenReturn(Optional.empty());

        final Optional<UserAccount> foundUserAccount = userAccountService.getUserAccountById(100L);
        Assert.assertFalse(foundUserAccount.isPresent());
    }
}