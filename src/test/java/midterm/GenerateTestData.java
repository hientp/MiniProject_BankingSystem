package midterm;

import midterm.models.Address;
import midterm.models.Transaction;
import midterm.models.TransactionPartners;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    List<Address> addressList = new ArrayList<Address>();
    Transaction transaction;
    TransactionPartners transactionPartner1, transactionPartner2;
    List<CheckingAccount> checkingAccountList = new ArrayList<CheckingAccount>();
    List<SavingsAccount> savingsAccountList = new ArrayList<SavingsAccount>();
    List<CreditCard> creditCardList = new ArrayList<CreditCard>();
    List<AccountHolder> accountHolderList = new ArrayList<AccountHolder>();
    List<Admin> adminList = new ArrayList<Admin>();
    List<ThirdParty> thirdPartyList = new ArrayList<ThirdParty>();

    @BeforeAll
    public void setUp() throws Exception{
        //40 Addressen, 60 AccountHolder, 3 Admins, 50 CA, 20 SA, 30 CC, 200 Transactions,400 transactionpartners
        //set up test-users
        //set up addresses
        for(int i=0;i<40;i++) {
            addressList.add(new Address("Default Str. "+i, "Berlin", "Germany", "15x"+i));
            addressRepository.save(addressList.get(i));
        }
        //setup accountholders
        int k=0;
        for(int i=0;i<60;i++) {
            if(i==40){k=0;}
            accountHolderList.add(new AccountHolder("Test_Customer_" + i, new Date(70, 5, 20), addressList.get(k)));
            k++;
            userRepository.save(accountHolderList.get(i));
        }
        //setup admins & thirdparty users
        for(int i=0;i<3;i++) {
            adminList.add(new Admin("Test_Admin_"+i,new Date(90, 4, 9),addressList.get(i)));
            thirdPartyList.add( new ThirdParty("Third_Party_"+i,new UUID(10,2)));
            userRepository.save(adminList.get(i));
            userRepository.save(thirdPartyList.get(i));
        }
        //set up all accounts
        BigDecimal balance0, balance1, interestRate1,interestRate2,creditLimit1,minimumBalance1;
        for(int i=0;i<50;i++){
            balance0=new BigDecimal("500").multiply(BigDecimal.valueOf(i+1));
            balance1=new BigDecimal("200").multiply(BigDecimal.valueOf(i+1));
            interestRate1= new BigDecimal("0.1").add(new BigDecimal("0.0003").multiply(BigDecimal.valueOf(i)));
            interestRate2= new BigDecimal("0.0025").add(new BigDecimal("0.001").multiply(BigDecimal.valueOf(i)));
            minimumBalance1= new BigDecimal("100").add(new BigDecimal("10").multiply(BigDecimal.valueOf(i)));
            creditLimit1= new BigDecimal("100").add(new BigDecimal("1000").multiply(BigDecimal.valueOf(i)));
            if(i<30) {
                checkingAccountList.add(new CheckingAccount(balance0, "secretKey_" + i, accountHolderList.get(i), null, Status.ACTIVE));
                creditCardList.add(new CreditCard(balance1,"secretKeyY_"+i,accountHolderList.get(i),null,interestRate1,creditLimit1));
                accountRepository.save(creditCardList.get(i));
            } else {
                checkingAccountList.add(new CheckingAccount(balance0, "secretKey_" + i, accountHolderList.get(i), accountHolderList.get(i-20), Status.ACTIVE));
                savingsAccountList.add(new SavingsAccount(balance0,"secretKeyY_"+i,accountHolderList.get(i),accountHolderList.get(i-20),interestRate2,minimumBalance1,Status.ACTIVE));
                accountRepository.save(savingsAccountList.get(i-30));
            }
            accountRepository.save(checkingAccountList.get(i));
        }
        //Transactions
        BigDecimal amount;
        for(int i=0;i<25;i++){
            amount=new BigDecimal("10").multiply(BigDecimal.valueOf(i));
            Utils.transactMoney(accountRepository,checkingAccountList.get(i),checkingAccountList.get(i+25),transactionRepository,transactionPartnersRepository,amount);
        }

    }

    @Test
    public void checkEntries(){

    }



}
