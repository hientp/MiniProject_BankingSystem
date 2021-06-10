package midterm.models.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import midterm.models.Address;
import midterm.models.TransactionPartners;
import midterm.models.enums.Period;
import midterm.models.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    @NotNull(message = "You must supply a primary Owner!")
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;

    @OneToMany(mappedBy="account")
    @JsonIgnore
    private List<TransactionPartners> transactionPartners;

    private LocalDateTime creationDate;
    private LocalDateTime interestRatePaymentDate;
    @NotNull
    private BigDecimal balance;
    @NotNull @NotEmpty
    private String secretKey;
    private BigDecimal interestRate;
    private BigDecimal penaltyFee = new BigDecimal("40");

    public Account() {
     this.creationDate= LocalDateTime.now();
    }

    public Account(BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) throws Exception{
        this.creationDate= LocalDateTime.now();
        this.balance=balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        setInterestRate(interestRate);
    }

    public Account(Integer id, LocalDateTime creationDate, LocalDateTime interestRatePaymentDate, BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) throws Exception{
        this.id = id;
        this.creationDate = creationDate;
        this.interestRatePaymentDate = interestRatePaymentDate;
        this.balance=balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        setInterestRate(interestRate);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {

        this.creationDate = creationDate;
    }


    public LocalDateTime getInterestRatePaymentDate() {
        return interestRatePaymentDate;
    }

    public void setInterestRatePaymentDate(LocalDateTime interestRatePaymentDate) {
        this.interestRatePaymentDate = interestRatePaymentDate;
    }

    public BigDecimal getBalance() throws Exception {
        return balance;
    }

    public void setBalance(BigDecimal balance) throws Exception {
            this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) throws Exception{
        this.interestRate = interestRate;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setNextInterestRatePaymentDate(Period period, Integer count){
        LocalDateTime lastDateTime = getInterestRatePaymentDate();
        switch(period) {
            case YEARLY:
                this.interestRatePaymentDate = lastDateTime.plusYears(count);
                break;
            case MONTHLY:
                this.interestRatePaymentDate = lastDateTime.plusMonths(count);
                break;
            default:
                System.err.println("Period must be monthly or yearly");
        }
    }

    public void changeBalance(BigDecimal valueToChange) throws Exception{
        setBalance(getBalance().add(valueToChange));
    }


}
