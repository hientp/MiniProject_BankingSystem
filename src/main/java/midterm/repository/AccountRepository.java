package midterm.repository;


import midterm.controller.dto.BalanceDTO;
import midterm.models.accounts.Account;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.users.FirstPartyUser;
import midterm.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    //Checking Accounts
    @Query(value = "from CheckingAccount where primaryOwner=:primaryOwner")
    List<CheckingAccount> findCheckingAccountByPrimaryOwner(FirstPartyUser primaryOwner);


    @Query(value = "from CheckingAccount")
    List<CheckingAccount> findAllCheckingAccounts();

    //Savings Accounts
    @Query(value = "from SavingsAccount where primaryOwner=:primaryOwner")
    List<SavingsAccount> findSavingsAccountByPrimaryOwner(FirstPartyUser primaryOwner);

    @Query(value = "from SavingsAccount")
    List<SavingsAccount> findAllSavingsAccounts();

    //Credit Cards
    @Query(value = "from CreditCard where primaryOwner=:primaryOwner")
    List<CreditCard> findCreditCardByPrimaryOwner(FirstPartyUser primaryOwner);

    @Query(value = "from CreditCard")
    List<CreditCard> findAllCreditCards();

    //Get Balance of account
    @Query(value = "Select balance from \n" +
            "((select balance from credit_card where id=:accountId) \n" +
            "union \n" +
            "(Select balance from checking_account where id=:accountId) \n" +
            "union\n" +
            "(Select balance from savings_account where id=:accountId)) a",nativeQuery = true)
    BigDecimal findBalanceOfAccount(Integer accountId);


    //Get Primary Owner by account id
    @Query(value = "select primary_owner_id \n" +
                    "from \n" +
                    "((select id, primary_owner_id from checking_account)\n" +
                    "UNION\n" +
                    "(select id, primary_owner_id from savings_account)\n" +
                    "UNION\n" +
                    "(select id, primary_owner_id from credit_card)) t\n" +
                    "where id=:AccountId",nativeQuery = true)
    Integer getPrimaryOwnerIdById(Integer AccountId);
//    @Query("select secondary_owner_id from Account where id=:AccountId")
//    Integer getSecondaryOwnerIdById(Integer AccountId);

}
