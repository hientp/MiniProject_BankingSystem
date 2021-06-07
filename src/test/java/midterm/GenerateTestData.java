package midterm;

import midterm.models.Address;
import midterm.models.Transaction;
import midterm.models.TransactionPartners;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.users.AccountHolder;
import midterm.models.users.Admin;
import midterm.models.users.ThirdParty;
import midterm.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = "patrick")
public class GenerateTestData {

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

    //Init class instances
    Address address;
    Transaction transaction;
    TransactionPartners transactionPartner1, transactionPartner2;
    CheckingAccount checkingAccount;
    SavingsAccount savingsAccount;
    CreditCard creditCard;
    AccountHolder accountHolder;
    Admin admin;
    ThirdParty thirdParty;

    @BeforeAll
    public void setUp(){
        //set up test-users
        address = new Address("Default Str.9","Berlin","Germany","15xxx");
        addressRepository.save(address);
        accountHolder = new AccountHolder("TestCustomer",new Date(70,5,20),address);
        userRepository.save(accountHolder);



    }

    @Test
    public void checkEntries(){

    }



}
