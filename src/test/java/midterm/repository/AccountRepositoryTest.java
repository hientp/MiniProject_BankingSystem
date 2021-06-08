package midterm.repository;

import midterm.models.Address;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = "patrick")
class AccountRepositoryTest {

    //Init Repos
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeAll
    public void setUp() throws Exception{
        //a) Checking Accounts
        Address address = new Address("Default Str. 1", "Berlin", "Germany", "15x");
        addressRepository.save(address);
        AccountHolder accountHolder= new AccountHolder("Test_Customer_2", new Date(70, 5, 20), address);
        userRepository.save(accountHolder);
        BigDecimal balance0=new BigDecimal("500");
        CheckingAccount firstCA = new CheckingAccount(balance0, "secretKey", accountHolder, null, Status.ACTIVE);
        //b) Savings Accounts
        BigDecimal balance1=new BigDecimal("1200");
        BigDecimal minimumBalance1=new BigDecimal("1000");
        BigDecimal interestRate1=new BigDecimal("0.5");
        SavingsAccount firstSA = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        //c) Credit Card
        balance1=new BigDecimal("1000");
        BigDecimal creditLimit=new BigDecimal("1000");
        interestRate1=new BigDecimal("0.2");
        CreditCard firstCC = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);


        accountRepository.save(firstCA);
        accountRepository.save(firstSA);
        accountRepository.save(firstCC);
    }

    @Test
    public void testRepoTests(){
        assertEquals(accountRepository.findAll().size(),3);
    }

}