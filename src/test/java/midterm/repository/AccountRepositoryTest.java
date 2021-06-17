package midterm.repository;

import midterm.models.Address;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;
import midterm.models.users.FirstPartyUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = {"patrick","hien","stefan"})
class AccountRepositoryTest {

    //Init Repos
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;

    CheckingAccount firstCA;
    SavingsAccount firstSA;
    CreditCard firstCC;

    @BeforeAll
    public void setUp() throws Exception{
        //a) Checking Accounts
        Address address = new Address("Default Str. 1", "Berlin", "Germany", "15x");
        addressRepository.save(address);
        FirstPartyUser accountHolder= new AccountHolder("Test_Customer_2", new Date(70, 5, 20), address);
        userRepository.save(accountHolder);
        BigDecimal balance0=new BigDecimal("500");
        firstCA = new CheckingAccount(balance0, "secretKey", accountHolder, null, Status.ACTIVE);
        //b) Savings Accounts
        BigDecimal balance1=new BigDecimal("1200");
        BigDecimal minimumBalance1=new BigDecimal("1000");
        BigDecimal interestRate1=new BigDecimal("0.5");
        firstSA = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        //c) Credit Card
        balance1=new BigDecimal("1000");
        BigDecimal creditLimit=new BigDecimal("1000");
        interestRate1=new BigDecimal("0.2");
        firstCC = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);


        accountRepository.save(firstCA);
        accountRepository.save(firstSA);
        accountRepository.save(firstCC);
    }

    @Test
    public void testRepoTests(){
        assertEquals(accountRepository.findAll().size(),3);
    }

    @Test
    void findCheckingAccountByPrimaryOwnerID() throws Exception {
        List<CheckingAccount> CA = accountRepository.findCheckingAccountByPrimaryOwner(firstCA.getPrimaryOwner());

        assertEquals(new BigDecimal("500.00"),CA.get(0).getBalance());
    }

    @Test
    void findAllCheckingAccounts() {
    }

    @Test
    void findSavingsAccountByPrimaryOwnerID() throws Exception {
        List<SavingsAccount> SA = accountRepository.findSavingsAccountByPrimaryOwner(firstSA.getPrimaryOwner());

        assertEquals(new BigDecimal("1200.00"),SA.get(0).getBalance());
    }

    @Test
    void findAllSavingsAccounts() {
    }

    @Test
    void findCreditCardByPrimaryOwnerID() throws Exception {
        List<CreditCard> CC = accountRepository.findCreditCardByPrimaryOwner(firstCC.getPrimaryOwner());

        assertEquals(new BigDecimal("1000.00"),CC.get(0).getBalance());
    }

    @Test
    void findAllCreditCards() {
    }

    @Test
    void findBalanceOfAccount() {
    }

    @Test
    void getPrimaryOwnerIdById() {
    }
}