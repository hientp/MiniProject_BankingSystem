package midterm.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;




    private LocalDateTime creationDate;
    private LocalDateTime interestRatePaymentDate;
    private BigDecimal balance;
    private String secretKey;
    private String primaryOwner;
    private String secondaryOwner;
    private BigDecimal interestRate;
    private BigDecimal penaltyFee = new BigDecimal("40");

    public Account() {
     this.creationDate= LocalDateTime.now();
    }

    public Account(BigDecimal balance, String secretKey, String primaryOwner, String secondaryOwner, BigDecimal interestRate) {
        this.creationDate= LocalDateTime.now();
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.interestRate = interestRate;
    }

    public Account(Integer id, LocalDateTime creationDate, LocalDateTime interestRatePaymentDate, BigDecimal balance, String secretKey, String primaryOwner, String secondaryOwner, BigDecimal interestRate) {
        this.id = id;
        this.creationDate = creationDate;
        this.interestRatePaymentDate = interestRatePaymentDate;
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.interestRate = interestRate;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(String primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public String getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(String secondaryOwner) {
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


}
