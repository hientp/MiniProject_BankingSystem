package midterm.repository;

import midterm.models.CheckingAccount;
import midterm.models.CreditCard;
import midterm.models.SavingsAccount;
import midterm.repository.AccountRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = "patrick")
class AccountRepositoryTest {

    //Init Repos
    @Autowired
    AccountRepository accountRepository;

    @BeforeAll
    public void setUp() throws Exception{
        CheckingAccount firstCA = new CheckingAccount();
        SavingsAccount firstSA = new SavingsAccount();
        CreditCard firstCC= new CreditCard();

        accountRepository.save(firstCA);
        accountRepository.save(firstSA);
        accountRepository.save(firstCC);
    }

    @Test
    public void testRepoTests(){

    }

}