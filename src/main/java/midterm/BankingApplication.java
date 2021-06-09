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
    // http://localhost:8080/banking/testData/
    //b) Get Account Information for a user
    //All CheckingAccounts
    //http://localhost:8080//banking/checking_accounts


}