package midterm.repository;


import midterm.models.accounts.Account;
import midterm.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

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
