package midterm.models;

import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;
import org.apache.tomcat.jni.Local;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = {"patrick","hien","stefan"})
class AccountTests {

    CheckingAccount testCheckingAccount;
    CreditCard testCreditCard;
    SavingsAccount testSavingsAccount;

    AccountHolder accountHolder1a ,accountHolder1b;
    AccountHolder accountHolder2a ,accountHolder2b;
    AccountHolder accountHolder3a ,accountHolder3b;

    Address address1, address2, address3;


    @BeforeAll
    public void setUp() throws Exception{

        testCheckingAccount=new CheckingAccount();
        testCreditCard= new CreditCard();
        testSavingsAccount= new SavingsAccount();

        address1= new Address("Default Str. 7","London","UK","yyyxxx");
        address2= new Address("Default Str. 1","Paris","France","aaaxxx");
        address3= new Address("Default Str. 2","Berlin","Germany","yyybbb");

        accountHolder1a= new AccountHolder("Patrick Paulson",new Date(90,05,10),address1);
        accountHolder1b= new AccountHolder("Paul Patrickson",new Date(90,04,10),address1);

        accountHolder2a= new AccountHolder("Patrick Paulson1",new Date(90,05,10),address2);
        accountHolder2b= new AccountHolder("Paul Patrickson1",new Date(90,04,10),address2);

        accountHolder3a= new AccountHolder("Patrick Paulson2",new Date(90,05,10),address3);
        accountHolder3b= new AccountHolder("Paul Patrickson2",new Date(90,04,10),address3);


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
        testCheckingAccount.setPrimaryOwner(accountHolder1a);
        testCheckingAccount.setSecondaryOwner(accountHolder1b);
        //Get the information
        assertEquals(testCheckingAccount.getId(),1);
        assertEquals(testCheckingAccount.getBalance(),new BigDecimal("2000"));
        assertEquals(testCheckingAccount.getMinimumBalance(),new BigDecimal("250"));
        assertEquals(testCheckingAccount.getStatus(), Status.ACTIVE);
        assertEquals(testCheckingAccount.getMonthlyMaintenanceFee(),new BigDecimal("12"));
        assertEquals(testCheckingAccount.getPenaltyFee(),new BigDecimal("40"));
        assertEquals(testCheckingAccount.getSecretKey(),"test1234");
        assertEquals(testCheckingAccount.getPrimaryOwner().getName(),"Patrick Paulson");
        assertEquals(testCheckingAccount.getSecondaryOwner().getName(),"Paul Patrickson");
        //Initialize Savings Account
        //Check all default parameters
        assertEquals(testSavingsAccount.getMonthlyMaintenanceFee(),new BigDecimal("0"));
        //Initialize the other parameter
        testSavingsAccount.setId(2);
        testSavingsAccount.setBalance(new BigDecimal("3000"));
        testSavingsAccount.setSecretKey("testxxx");
        testSavingsAccount.setPrimaryOwner(accountHolder2a);
        testSavingsAccount.setSecondaryOwner(accountHolder2b);
        //Get the information
        assertEquals(testSavingsAccount.getId(),2);
        assertEquals(testSavingsAccount.getBalance(),new BigDecimal("3000"));
        assertEquals(testSavingsAccount.getMinimumBalance(),new BigDecimal("1000"));
        assertEquals(testSavingsAccount.getInterestRate(),new BigDecimal("0.0025"));
        assertEquals(testSavingsAccount.getStatus(), Status.ACTIVE);
        assertEquals(testSavingsAccount.getPenaltyFee(),new BigDecimal("40"));
        assertEquals(testSavingsAccount.getSecretKey(),"testxxx");
        assertEquals(testSavingsAccount.getPrimaryOwner().getName(),"Patrick Paulson1");
        assertEquals(testSavingsAccount.getSecondaryOwner().getName(),"Paul Patrickson1");
        //Initialize Credit Card Account
        //Initialize the other parameter
        testCreditCard.setId(3);
        testCreditCard.setBalance(new BigDecimal("4000"));
        testCreditCard.setSecretKey("testyyy");
        testCreditCard.setPrimaryOwner(accountHolder3a);
        testCreditCard.setSecondaryOwner(accountHolder3b);
        //Get the information
        assertEquals(testCreditCard.getId(),3);
        assertEquals(testCreditCard.getBalance(),new BigDecimal("4000"));
        assertEquals(testCreditCard.getInterestRate(),new BigDecimal("0.2"));
        assertEquals(testCreditCard.getPenaltyFee(),new BigDecimal("40"));
        assertEquals(testCreditCard.getSecretKey(),"testyyy");
        assertEquals(testCreditCard.getPrimaryOwner().getName(),"Patrick Paulson2");
        assertEquals(testCreditCard.getSecondaryOwner().getName(),"Paul Patrickson2");
        assertEquals(testCreditCard.getCreditLimit(),new BigDecimal("100"));
    }

    @Test
    public void testValidation() throws Exception{
        //Initialize validation examples
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //a) Checking Accounts
        Address address = new Address("Default Str. 1", "Berlin", "Germany", "15x");
        AccountHolder accountHolder= new AccountHolder("Test_Customer_2", new Date(70, 5, 20), address);
        BigDecimal balance0=new BigDecimal("500");
        //Check with no validationerror
        CheckingAccount checkingAccount = new CheckingAccount(balance0, "secretKey", accountHolder, null, Status.ACTIVE);
        Set violations = validator.validate(checkingAccount);
        assertTrue(violations.isEmpty());

        //Check setting interest rate for checkingAccounts to interestRate other than 0
        CheckingAccount finalCheckingAccount = checkingAccount;
        Exception exception = assertThrows(Exception.class, () -> {
            finalCheckingAccount.setInterestRate(new BigDecimal("0.2"));
        });

        assertTrue(exception.getMessage().contains("Checking Accounts have an interest rate of 0! Rate of 0 has been set."));

        //Check with null primary owner
        checkingAccount = new CheckingAccount(balance0, "secretKey", null, null, Status.ACTIVE);
        violations = validator.validate(checkingAccount);
        assertFalse(violations.isEmpty());

        //b) Savings Account
        BigDecimal balance1=new BigDecimal("1000");
        BigDecimal minimumBalance1=new BigDecimal("1000");
        BigDecimal interestRate1=new BigDecimal("0.5");

        //Check with no validationerror
        SavingsAccount savingsAccount = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        violations = validator.validate(savingsAccount);
        assertTrue(violations.isEmpty());

        //Check with empty secretKey
        savingsAccount = new SavingsAccount(balance1,"",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        violations = validator.validate(savingsAccount);
        assertFalse(violations.isEmpty());

        //Check with interestRate>0.5
        interestRate1=new BigDecimal("0.6");
        savingsAccount = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        violations = validator.validate(savingsAccount);
        assertFalse(violations.isEmpty());

        //Check with interestRate<0 -> no validation error
        interestRate1=new BigDecimal("-0.6");
        savingsAccount = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        violations = validator.validate(savingsAccount);
        assertTrue(violations.isEmpty());

        //Check with minimumBalance>1000
        minimumBalance1=new BigDecimal("1100");
        savingsAccount = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        violations = validator.validate(savingsAccount);
        assertFalse(violations.isEmpty());

        //Check with minimumBalance<100
        minimumBalance1=new BigDecimal("50");
        savingsAccount = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        violations = validator.validate(savingsAccount);
        assertFalse(violations.isEmpty());

        //c) Credit Card
        balance1=new BigDecimal("1000");
        BigDecimal creditLimit=new BigDecimal("1000");
        interestRate1=new BigDecimal("0.2");

        //Check with no validationerror
        CreditCard creditCard = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);
        violations = validator.validate(creditCard);
        assertTrue(violations.isEmpty());

        //Check with interestRate>0.2
        interestRate1=new BigDecimal("0.3");
        creditCard = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);
        violations = validator.validate(creditCard);
        assertFalse(violations.isEmpty());

        //Check with interestRate<0.1
        interestRate1=new BigDecimal("0.05");
        creditCard = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);
        violations = validator.validate(creditCard);
        assertFalse(violations.isEmpty());

        //Check with creditLimit<100
        interestRate1=new BigDecimal("0.2");
        creditLimit=new BigDecimal("50");
        creditCard = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);
        violations = validator.validate(creditCard);
        assertFalse(violations.isEmpty());

        //Check with creditLimit>100000
        creditLimit=new BigDecimal("100001");
        creditCard = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);
        violations = validator.validate(creditCard);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void minimumBalance() throws Exception{
        //a) Checking Accounts
        Address address = new Address("Default Str. 1", "Berlin", "Germany", "15x");
        AccountHolder accountHolder= new AccountHolder("Test_Customer_2", new Date(70, 5, 20), address);
        BigDecimal balance0=new BigDecimal("500");
        CheckingAccount checkingAccount = new CheckingAccount(balance0, "secretKey", accountHolder, null, Status.ACTIVE);

        checkingAccount.changeBalance(new BigDecimal("-250"));
        assertEquals(checkingAccount.getBalance(),new BigDecimal("250"));
        checkingAccount.changeBalance(new BigDecimal("-50"));
        assertEquals(checkingAccount.getBalance(),new BigDecimal("160"));
        //Wenn die MinimumBalance erreicht ist, wird keine weitere penaltyFee abgezogen, bis die balance wieder über
        //dem Mindestbetrag liegt und erst dann wieder darunter fällt.
        checkingAccount.changeBalance(new BigDecimal("-50"));
        assertEquals(checkingAccount.getBalance(),new BigDecimal("110"));
        checkingAccount.changeBalance(new BigDecimal("140"));
        assertEquals(checkingAccount.getBalance(),new BigDecimal("250"));
        checkingAccount.changeBalance(new BigDecimal("-50"));
        assertEquals(checkingAccount.getBalance(),new BigDecimal("160"));

        //b) Savings Accounts
        BigDecimal balance1=new BigDecimal("1200");
        BigDecimal minimumBalance1=new BigDecimal("1000");
        BigDecimal interestRate1=new BigDecimal("0.5");
        SavingsAccount savingsAccount = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);

        savingsAccount.changeBalance(new BigDecimal("-200"));
        assertEquals(savingsAccount.getBalance(),new BigDecimal("1000"));
        savingsAccount.changeBalance(new BigDecimal("-200"));
        assertEquals(savingsAccount.getBalance(),new BigDecimal("760"));
        //Wenn die MinimumBalance erreicht ist, wird keine weitere penaltyFee abgezogen, bis die balance wieder über
        //dem Mindestbetrag liegt und erst dann wieder darunter fällt.
        savingsAccount.changeBalance(new BigDecimal("-50"));
        assertEquals(savingsAccount.getBalance(),new BigDecimal("710"));
        savingsAccount.changeBalance(new BigDecimal("290"));
        assertEquals(savingsAccount.getBalance(),new BigDecimal("1000"));
        savingsAccount.changeBalance(new BigDecimal("-50"));
        assertEquals(savingsAccount.getBalance(),new BigDecimal("910"));

    }

    @Test
    public void testLowerBalanceBoundary() throws Exception{
        //a) Checking Accounts
        Address address = new Address("Default Str. 1", "Berlin", "Germany", "15x");
        AccountHolder accountHolder= new AccountHolder("Test_Customer_2", new Date(70, 5, 20), address);
        BigDecimal balance0=new BigDecimal("500");
        CheckingAccount checkingAccount = new CheckingAccount(balance0, "secretKey", accountHolder, null, Status.ACTIVE);

        checkingAccount.changeBalance(new BigDecimal("-460"));
        assertEquals(checkingAccount.getBalance(),new BigDecimal("0"));

        CheckingAccount finalCheckingAccount = checkingAccount;
        Exception exception = assertThrows(Exception.class, () -> {
            finalCheckingAccount.changeBalance(new BigDecimal("-1"));
        });

        assertTrue(exception.getMessage().contains("The balance of this account would drop below 0! Transaction denied."));
        assertEquals(finalCheckingAccount.getBalance(),new BigDecimal("0"));

        //b) Savings Accounts
        BigDecimal balance1=new BigDecimal("1200");
        BigDecimal minimumBalance1=new BigDecimal("1000");
        BigDecimal interestRate1=new BigDecimal("0.5");
        SavingsAccount savingsAccount = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);

        savingsAccount.changeBalance(new BigDecimal("-1160"));
        assertEquals(savingsAccount.getBalance(),new BigDecimal("0"));

        SavingsAccount finalSavingsAccount = savingsAccount;
        exception = assertThrows(Exception.class, () -> {
            finalSavingsAccount.changeBalance(new BigDecimal("-1"));
        });

        assertTrue(exception.getMessage().contains("The balance of this account would drop below 0! Transaction denied."));
        assertEquals(finalSavingsAccount.getBalance(),new BigDecimal("0"));

        //c) Credit Card
        balance1=new BigDecimal("1000");
        BigDecimal creditLimit=new BigDecimal("1000");
        interestRate1=new BigDecimal("0.2");
        CreditCard creditCard = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);

        creditCard.changeBalance(new BigDecimal("-2000"));
        assertEquals(creditCard.getBalance(),new BigDecimal("-1000"));

        CreditCard finalCreditCard = creditCard;
        exception = assertThrows(Exception.class, () -> {
            finalCreditCard.changeBalance(new BigDecimal("-1"));
        });

        assertTrue(exception.getMessage().contains("The credit limit for this account is 1000 ! It is not allowed that the balance exceeds the limit!"));
        assertEquals(finalCreditCard.getBalance(),new BigDecimal("-1000"));

    }

    @Test
    public void testInterestPayments() throws Exception {
        //Create new account one,two,three years ago from today and test if the correct interest is calculated
        //a) Checking Accounts
        Address address = new Address("Default Str. 1", "Berlin", "Germany", "15x");
        AccountHolder accountHolder= new AccountHolder("Test_Customer_2", new Date(70, 5, 20), address);
        BigDecimal balance0=new BigDecimal("1000");
        CheckingAccount checkingAccount = new CheckingAccount(balance0, "secretKey", accountHolder, null, Status.ACTIVE);
        LocalDateTime thirteenMonthsBeforeNow = LocalDateTime.now().minusMonths(13);
        checkingAccount.setCreationDate(thirteenMonthsBeforeNow);

        LocalDateTime date1= checkingAccount.getInterestRatePaymentDate();
        //Balance minus maintenance fee
        LocalDateTime date2= LocalDateTime.now().minusMonths(1);
        assertEquals(date1.withNano(0),date2.withNano(0));
        assertEquals(checkingAccount.getBalance(),balance0.add(new BigDecimal("-12")));
        date1= checkingAccount.getInterestRatePaymentDate();
        date2= LocalDateTime.now().plusMonths(11);
        assertEquals(date1.withNano(0),date2.withNano(0));

        //SavingsAccount
        BigDecimal balance1=new BigDecimal("1000");
        BigDecimal minimumBalance1=new BigDecimal("1000");
        BigDecimal interestRate1=new BigDecimal("0.5");
        SavingsAccount savingsAccount = new SavingsAccount(balance1,"secretKey",accountHolder,null,interestRate1,minimumBalance1,Status.ACTIVE);
        //13months before now
        thirteenMonthsBeforeNow = LocalDateTime.now().minusMonths(13);
        savingsAccount.setCreationDate(thirteenMonthsBeforeNow);

        date1= savingsAccount.getInterestRatePaymentDate();
        date2= LocalDateTime.now().minusMonths(1);
        assertEquals(date1.withNano(0),date2.withNano(0));
        assertEquals(savingsAccount.getBalance(),new BigDecimal("1005.000"));
        date1= savingsAccount.getInterestRatePaymentDate();
        date2= LocalDateTime.now().plusMonths(11);
        assertEquals(date1.withNano(0),date2.withNano(0));

        //25months before now
        LocalDateTime twentyFiveMonthsBeforeNow = LocalDateTime.now().minusMonths(25);
        savingsAccount.setBalance(new BigDecimal("1000"));
        savingsAccount.setCreationDate(twentyFiveMonthsBeforeNow);

        date1= savingsAccount.getInterestRatePaymentDate();
        date2= LocalDateTime.now().minusMonths(13);
        assertEquals(date1.withNano(0),date2.withNano(0));
        assertEquals(savingsAccount.getBalance(),new BigDecimal("1010.025000"));
        date1= savingsAccount.getInterestRatePaymentDate();
        date2= LocalDateTime.now().plusMonths(11);
        assertEquals(date1.withNano(0),date2.withNano(0));

        //c) Credit Card
        balance1=new BigDecimal("-100");
        BigDecimal creditLimit=new BigDecimal("1000");
        interestRate1=new BigDecimal("0.2");
        CreditCard creditCard = new CreditCard(balance1,"secretKey",accountHolder,null,interestRate1,creditLimit);
        //13months before now
        thirteenMonthsBeforeNow = LocalDateTime.now().minusMonths(13);
        savingsAccount.setCreationDate(thirteenMonthsBeforeNow);


    }

    @Test
    public void testMonthlyMaintenanceFee() throws Exception {
        //For checkingAccounts the maintenanceFee needs to be deducted from the balance each month
        //Create new account one,two,three years ago from today and test if the correct interest is calculated
        //a) Checking Accounts
        Address address = new Address("Default Str. 1", "Berlin", "Germany", "15x");
        AccountHolder accountHolder= new AccountHolder("Test_Customer_3", new Date(70, 5, 20), address);
        BigDecimal balance0=new BigDecimal("1000");
        CheckingAccount checkingAccount = new CheckingAccount(balance0, "secretKey", accountHolder, null, Status.ACTIVE);
        LocalDateTime FiveWeeksBeforeNow = LocalDateTime.now().minusWeeks(5);
        checkingAccount.setCreationDate(FiveWeeksBeforeNow);

        assertEquals(new BigDecimal("988"),checkingAccount.getBalance());


    }







}