package midterm.models;

import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = "patrick")
class AccountTests {

    CheckingAccount testCheckingAccount;
    CreditCard testCreditCard;
    SavingsAccount testSavingsAccount;


    @BeforeAll
    public void setUp() throws Exception{

        testCheckingAccount=new CheckingAccount();
        testCreditCard= new CreditCard();
        testSavingsAccount= new SavingsAccount();
    }

    @Test
    public void basicAccountDateTests(){
        //CreationDate
        assertTrue(testCheckingAccount.getCreationDate().isAfter(LocalDateTime.of(2021, Month.JUNE,07,0,0,0)));
        assertTrue(testCreditCard.getCreationDate().isAfter(LocalDateTime.of(2021, Month.JUNE,07,0,0,0)));
        assertTrue(testSavingsAccount.getCreationDate().isAfter(LocalDateTime.of(2021, Month.JUNE,07,0,0,0)));
        //NextDateForInterestPayment
        assertTrue(testCheckingAccount.getInterestRatePaymentDate().isAfter(LocalDateTime.of(2022, Month.JUNE,07,0,0,0)));
        assertTrue(testCreditCard.getInterestRatePaymentDate().isAfter(LocalDateTime.of(2021, Month.JULY,07,0,0,0)));
        assertTrue(testSavingsAccount.getInterestRatePaymentDate().isAfter(LocalDateTime.of(2022, Month.JUNE,07,0,0,0)));
    }

    @Test
    public void basicInitTest() throws Exception{
        //Initialize Checking Account
        //Check all default parameters
        assertEquals(new BigDecimal("0"),testCheckingAccount.getInterestRate());
        //Initialize the other parameter
        testCheckingAccount.setId(1);
        testCheckingAccount.setBalance(new BigDecimal("2000"));
        testCheckingAccount.setSecretKey("test1234");
        testCheckingAccount.setPrimaryOwner("Patrick Paulson");
        testCheckingAccount.setSecondaryOwner("Paul Patrickson");
        //Get the information
        assertEquals(testCheckingAccount.getId(),1);
        assertEquals(testCheckingAccount.getBalance(),new BigDecimal("2000"));
        assertEquals(testCheckingAccount.getMinimumBalance(),new BigDecimal("250"));
        assertEquals(testCheckingAccount.getStatus(), Status.ACTIVE);
        assertEquals(testCheckingAccount.getMonthlyMaintenanceFee(),new BigDecimal("12"));
        assertEquals(testCheckingAccount.getPenaltyFee(),new BigDecimal("40"));
        assertEquals(testCheckingAccount.getSecretKey(),"test1234");
        assertEquals(testCheckingAccount.getPrimaryOwner(),"Patrick Paulson");
        assertEquals(testCheckingAccount.getSecondaryOwner(),"Paul Patrickson");
        //Initialize Savings Account
        //Check all default parameters
        assertEquals(testSavingsAccount.getMonthlyMaintenanceFee(),new BigDecimal("0"));
        //Initialize the other parameter
        testSavingsAccount.setId(2);
        testSavingsAccount.setBalance(new BigDecimal("3000"));
        testSavingsAccount.setSecretKey("testxxx");
        testSavingsAccount.setPrimaryOwner("Patrick Paulson1");
        testSavingsAccount.setSecondaryOwner("Paul Patrickson1");
        //Get the information
        assertEquals(testSavingsAccount.getId(),2);
        assertEquals(testSavingsAccount.getBalance(),new BigDecimal("3000"));
        assertEquals(testSavingsAccount.getMinimumBalance(),new BigDecimal("1000"));
        assertEquals(testSavingsAccount.getInterestRate(),new BigDecimal("0.0025"));
        assertEquals(testSavingsAccount.getStatus(), Status.ACTIVE);
        assertEquals(testSavingsAccount.getPenaltyFee(),new BigDecimal("40"));
        assertEquals(testSavingsAccount.getSecretKey(),"testxxx");
        assertEquals(testSavingsAccount.getPrimaryOwner(),"Patrick Paulson1");
        assertEquals(testSavingsAccount.getSecondaryOwner(),"Paul Patrickson1");
        //Initialize Credit Card Account
        //Initialize the other parameter
        testCreditCard.setId(3);
        testCreditCard.setBalance(new BigDecimal("4000"));
        testCreditCard.setSecretKey("testyyy");
        testCreditCard.setPrimaryOwner("Patrick Paulson3");
        testCreditCard.setSecondaryOwner("Paul Patrickson3");
        //Get the information
        assertEquals(testCreditCard.getId(),3);
        assertEquals(testCreditCard.getBalance(),new BigDecimal("4000"));
        assertEquals(testCreditCard.getInterestRate(),new BigDecimal("0.2"));
        assertEquals(testCreditCard.getPenaltyFee(),new BigDecimal("40"));
        assertEquals(testCreditCard.getSecretKey(),"testyyy");
        assertEquals(testCreditCard.getPrimaryOwner(),"Patrick Paulson3");
        assertEquals(testCreditCard.getSecondaryOwner(),"Paul Patrickson3");
        assertEquals(testCreditCard.getCreditLimit(),new BigDecimal("100"));
    }



}