package midterm.repository;

import midterm.Utils;
import midterm.models.*;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Alignment;
import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;
import midterm.models.users.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = "patrick")
class TransactionRepositoryTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionPartnersRepository transactionPartnersRepository;

    Address address1,address2;
    AccountHolder a1,a2,a3;
    CheckingAccount CA_1,CA_2,CA_3;
    SavingsAccount SA_1, SA_2, SA_3;
    CreditCard CC_1, CC_2, CC_3;

    @BeforeAll
    public void setUp() throws Exception{
        //Init addresses
        address1= new Address("Default Str.1","Berlin","Germany","15xxx");
        address2= new Address("Default Str.2","Berlin","Germany","15xxx");
        a1 = new AccountHolder("Anton Alligator",new Date(100,5,10),address1);
        CA_1 = new CheckingAccount(new BigDecimal("2000"),"secretKey123",a1,null, Status.ACTIVE);
        SA_1 = new SavingsAccount( new BigDecimal("10000"),"banane123",a1,null,new BigDecimal("0.003"),new BigDecimal("1000"),Status.ACTIVE);
        CC_1= new CreditCard( new BigDecimal("3000"),"obst321",a1,null,new BigDecimal("0.2"),new BigDecimal("200"));

        addressRepository.save(address1);
        userRepository.save(a1);
        accountRepository.save(CA_1);
        accountRepository.save(SA_1);
        accountRepository.save(CC_1);

        a2 = new AccountHolder("Bernd Babyelefant",new Date(100,5,10),address2);
        CA_2 = new CheckingAccount(new BigDecimal("1000"),"secretKey456",a2,null, Status.ACTIVE);
        SA_2 = new SavingsAccount( new BigDecimal("20000"),"banane456",a2,null,new BigDecimal("0.001"),new BigDecimal("1000"),Status.ACTIVE);
        CC_2= new CreditCard( new BigDecimal("1000"),"obst654",a2,null,new BigDecimal("0.1"),new BigDecimal("300"));

        addressRepository.save(address2);
        userRepository.save(a2);
        accountRepository.save(CA_2);
        accountRepository.save(SA_2);
        accountRepository.save(CC_2);

        a3 = new AccountHolder("Chrissy Chamäleon",new Date(900,2,10),address1);
        CA_3 = new CheckingAccount(new BigDecimal("20000"),"secretKey789",a3,null, Status.ACTIVE);
        SA_3 = new SavingsAccount( new BigDecimal("100000"),"banane789",a3,null,new BigDecimal("0.002"),new BigDecimal("500"),Status.ACTIVE);
        CC_3= new CreditCard( new BigDecimal("6000"),"obst987",a3,null,new BigDecimal("0.2"),new BigDecimal("400"));

        userRepository.save(a3);
        accountRepository.save(CA_3);
        accountRepository.save(SA_3);
        accountRepository.save(CC_3);
    }

    @Test
    public void testRepoTests() throws Exception {

        //Test normal functions
        Transaction transaction = new Transaction(java.time.LocalDateTime.now(),new BigDecimal("200"));
        transactionRepository.save(transaction);

        TransactionPartners transactionPartners1 = new TransactionPartners(CA_1,transaction, Alignment.SENDER);
        TransactionPartners transactionPartners2 = new TransactionPartners(CA_2,transaction,Alignment.RECEIVER);

        transactionPartnersRepository.save(transactionPartners1);
        transactionPartnersRepository.save(transactionPartners2);

        assertEquals(transactionRepository.findAll().size(),1);
        assertEquals(transactionPartnersRepository.findAll().size(),2);

        //Test utils functions
        //Default Balances CA1: 2000, CA2:1000 ; minimumBalance: 250
        Utils.transactMoney(CA_1,CA_2,transactionRepository,transactionPartnersRepository,new BigDecimal("500"));
        assertEquals(CA_1.getBalance(),new BigDecimal("1500"));
        assertEquals(CA_2.getBalance(),new BigDecimal("1500"));
        assertEquals(transactionRepository.findAll().size(),2);
        //Current Balances CA1: 1500, CA2:1500 ; minimumBalance: 250
        Utils.transactMoney(CA_1,CA_2,transactionRepository,transactionPartnersRepository,new BigDecimal("1250"));
        assertEquals(CA_1.getBalance(),new BigDecimal("250"));
        assertEquals(CA_2.getBalance(),new BigDecimal("2750"));
        assertEquals(transactionRepository.findAll().size(),3);
        //Current Balances CA1: 250, CA2:2750 ; minimumBalance: 250
        Utils.transactMoney(CA_1,CA_2,transactionRepository,transactionPartnersRepository,new BigDecimal("50"));
        assertEquals(CA_1.getBalance(),new BigDecimal("160"));
        assertEquals(CA_2.getBalance(),new BigDecimal("2800"));
        assertEquals(transactionRepository.findAll().size(),4);
        //Current Balances CA1: 160, CA2:2800 ; minimumBalance: 250
        Utils.transactMoney(CA_2,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("2000"));
        assertEquals(CA_1.getBalance(),new BigDecimal("2160"));
        assertEquals(CA_2.getBalance(),new BigDecimal("800"));
        assertEquals(transactionRepository.findAll().size(),5);
        //Current Balances CA1: 2160, CA2:800 ; minimumBalance: 250
        Exception exception = assertThrows(Exception.class, () -> {
            Utils.transactMoney(CA_2,CA_1,transactionRepository,transactionPartnersRepository,new BigDecimal("1000"));
        });

        assertTrue(exception.getMessage().contains("The balance of this account would drop below 0! Transaction denied."));
        assertEquals(transactionRepository.findAll().size(),5);


    }


}