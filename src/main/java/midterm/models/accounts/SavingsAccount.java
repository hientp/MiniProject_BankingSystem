package midterm.models.accounts;

import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "SavingsAccount")
@Table(name = "savings_account")
public class SavingsAccount extends Account {
    private BigDecimal minimumBalance= new BigDecimal("1000");
    private BigDecimal monthlyMaintenanceFee= new BigDecimal("0");
    @Enumerated(EnumType.STRING)
    private Status status= Status.ACTIVE;

    public SavingsAccount() throws Exception{
        super();
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        setInterestRate(new BigDecimal("0.0025"));
    }

    public SavingsAccount(BigDecimal balance, String secretKey, String primaryOwner, String secondaryOwner, BigDecimal interestRate, BigDecimal minimumBalance,  Status status, AccountHolder accountHolder) {
        super(balance, secretKey, primaryOwner, secondaryOwner, interestRate,accountHolder);
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        this.minimumBalance = minimumBalance;
        this.status = status;
    }

    public SavingsAccount(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, String primaryOwner, String secondaryOwner, BigDecimal interestRate, BigDecimal minimumBalance,  Status status, AccountHolder accountHolder) {
        super(id, creationDate, nextDateForInterestPayment, balance, secretKey, primaryOwner, secondaryOwner, interestRate,accountHolder);
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        this.minimumBalance = minimumBalance;
        this.status = status;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee() {
        this.monthlyMaintenanceFee= new BigDecimal("0");
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
