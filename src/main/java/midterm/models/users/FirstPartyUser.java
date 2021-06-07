package midterm.models.users;

import midterm.models.Address;
import midterm.models.accounts.Account;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class FirstPartyUser extends User{
    private Date birthDay;

//    @ManyToMany(mappedBy = "firstpartyuser")
//    private Set<Address> addresses;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

//    @ManyToOne
//    private Set<Account> accounts;

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
