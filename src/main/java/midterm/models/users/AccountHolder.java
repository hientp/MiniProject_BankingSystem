package midterm.models.users;

import midterm.models.Address;
import midterm.models.accounts.Account;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name= "AccountHolder")
public class AccountHolder extends FirstPartyUser{

    @OneToMany(mappedBy = "accountHolder")
    Set<Account> accounts=  new HashSet<>();

    public AccountHolder() {
        super();
    }

    public AccountHolder(String name, Date birthDay, Address address) {
        super(name, birthDay,address);
    }
}
