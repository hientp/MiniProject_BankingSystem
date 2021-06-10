package midterm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }
    //--spring.profiles.active=patrick


    //a) Generate TestData
    //http://localhost:8080/banking/testData/
    //b) Get Account Information for a user
    //All CheckingAccounts, SavingsAccounts, CreditCards
    //http://localhost:8080/banking/checking_accounts/
    //http://localhost:8080/banking/savings_accounts/
    //http://localhost:8080/banking/credit_cards/
    //Create new account



    //Example Checking_Account:



    //Check balance of all accounts
    //http://localhost:8080/banking/account_balance/{id}


}