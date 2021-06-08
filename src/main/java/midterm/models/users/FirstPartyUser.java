package midterm.models.users;

import midterm.models.Address;
import midterm.models.accounts.Account;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class FirstPartyUser extends User{
    private Date birthDay;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "primaryOwner")
    Set<Account> firstPartyAccounts=  new HashSet<>();

    @OneToMany(mappedBy = "secondaryOwner")
    Set<Account> secondPartyAccounts=  new HashSet<>();


    public FirstPartyUser() {
    super();
    }

    public FirstPartyUser(String name, Date birthDay, Address address) {
        super(name);
        this.birthDay = birthDay;
        this.address=address;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Address getAddresses() {
        return address;
    }

    public void setAddresses(Address address) {
        this.address = address;
    }
}
