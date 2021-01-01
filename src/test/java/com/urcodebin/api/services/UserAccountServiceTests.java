package com.urcodebin.api.services;

import com.urcodebin.api.controllers.requestbody.SignupRequestBody;
import com.urcodebin.api.entities.UserAccount;
import com.urcodebin.api.error.exception.AccountInformationTakenException;
import com.urcodebin.api.error.exception.UserAccountNotFoundException;
import com.urcodebin.api.repository.UserAccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceTests {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    UserAccount testAccount;
    SignupRequestBody signupRequestBody;

    @Before
    public void setup() {
        testAccount = new UserAccount();
        testAccount.setUsername("Test Username");
        testAccount.setEmail("testemail@gmail.com");
        testAccount.setId(105L);

        signupRequestBody = new SignupRequestBody();
        signupRequestBody.setUsername("Test Username");
        signupRequestBody.setEmail("testemail@gmail.com");
        signupRequestBody.setPassword("password");
    }

    @Test
    public void getUserAccountByIdWithCorrectIdReturnsRightUserAccount() {
        when(userAccountRepository.findById(testAccount.getId())).thenReturn(Optional.of(testAccount));

        final UserAccount foundUserAccount = userAccountService.getUserAccountById(testAccount.getId());
        Assert.assertEquals(foundUserAccount, testAccount);
    }

    @Test(expected = UserAccountNotFoundException.class)
    public void getUserAccountByIdWithWrongIdThrowsUserAccountNotFoundException() {
        when(userAccountRepository.findById(100L)).thenReturn(Optional.empty());

        final UserAccount foundUserAccount = userAccountService.getUserAccountById(100L);
    }

    @Test
    public void signupNewUserAccountWithValidSignupRequestBodySavesAndReturnsUserAccount() {
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(arg -> arg.getArgument(0));
        when(passwordEncoder.encode(any())).thenReturn("Encoded Password");

        final UserAccount userAccount = userAccountService.signupNewUserAccount(signupRequestBody);
        Assert.assertEquals(userAccount.getEmail(), signupRequestBody.getEmail());
        Assert.assertEquals(userAccount.getUsername(), signupRequestBody.getUsername());
        //Confirm that the password has been encoded and is not the same plain password.
        Assert.assertNotEquals(userAccount.getPassword(), signupRequestBody.getPassword());
    }

    @Test(expected = AccountInformationTakenException.class)
    public void signupNewUserAccountWithTakenUsernameThrowsAccountInformationTakenException() {
        when(userAccountRepository.existsByUsername(signupRequestBody.getUsername())).thenReturn(true);

        userAccountService.signupNewUserAccount(signupRequestBody);
    }

    @Test(expected = AccountInformationTakenException.class)
    public void signupNewUserAccountWithTakenEmailThrowsAccountInformationTakenException() {
        when(userAccountRepository.existsByEmail(signupRequestBody.getEmail())).thenReturn(true);

        userAccountService.signupNewUserAccount(signupRequestBody);
    }
}