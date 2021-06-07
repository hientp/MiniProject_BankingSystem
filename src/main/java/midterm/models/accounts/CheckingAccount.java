package midterm.models.accounts;

import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "CheckingAccount")
@Table(name = "checking_account")
public class CheckingAccount extends Account {
    private BigDecimal minimumBalance = new BigDecimal("250");
    private BigDecimal monthlyMaintenanceFee= new BigDecimal("12");
    @Enumerated(EnumType.STRING)
    private Status status= Status.ACTIVE;

    public CheckingAccount() throws Exception{
        super();
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        setInterestRate(new BigDecimal("0"));
    }

    public CheckingAccount(BigDecimal balance, String secretKey, String primaryOwner, String secondaryOwner, Status status, AccountHolder accountHolder) {
        super(balance, secretKey, primaryOwner, secondaryOwner, new BigDecimal("0"),accountHolder);
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        this.status = status;
    }

    public CheckingAccount(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, String primaryOwner, String secondaryOwner,  Status status, AccountHolder accountHolder) {
        super(id, creationDate, nextDateForInterestPayment, balance, secretKey, primaryOwner, secondaryOwner, new BigDecimal("0"),accountHolder); //test
        this.status = status;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }


    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void setInterestRate(BigDecimal interestRate) throws Exception{
        if (!interestRate.equals(new BigDecimal("0"))){
            throw new Exception("Checking Accounts have an interest rate of 0! Rate of 0 has been set.");
        }
        super.setInterestRate(new BigDecimal("0"));
    }
}
