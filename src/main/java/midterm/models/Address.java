package midterm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import midterm.models.users.FirstPartyUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Address")
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    private String street;
    private String city;
    private String country;
    private String zipCode;

//    @ManyToMany
//    @JoinTable(
//            name = "test_patrick",
//            joinColumns = @JoinColumn(name = "address_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    Set<FirstPartyUser> firstpartyuser;

    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private Set<FirstPartyUser> firstPartyUser =  new HashSet<>();

    public Address() {
    }

    public Address(String street, String city, String country, String zipCode) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
    }

    @JsonIgnore
    public Set<FirstPartyUser> getFirstpartyuser() {
        return firstPartyUser;
    }

    public void setFirstpartyuser(Set<FirstPartyUser> firstpartyuser) {
        this.firstPartyUser = firstpartyuser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
