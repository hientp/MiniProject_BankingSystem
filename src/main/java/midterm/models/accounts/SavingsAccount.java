package midterm.models.accounts;

import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "SavingsAccount")
@Table(name = "savings_account")
public class SavingsAccount extends Account {
    @DecimalMin(value = "100.0", inclusive = true,message="A MinumumBalance less than 100 is not allowed!")
    @DecimalMax(value = "1000.0", inclusive = true,message="A MinumumBalance more than 1000 is not allowed!")
    @NotNull
    private BigDecimal minimumBalance= new BigDecimal("1000");
    @NotNull
    private BigDecimal monthlyMaintenanceFee= new BigDecimal("0");

    @DecimalMax(value = "0.5", inclusive = true,message="An interestRate more than 0.5 is not allowed!")
    @NotNull
    private BigDecimal interestRate= new BigDecimal("0.0025");



    @Enumerated(EnumType.STRING)
    private Status status= Status.ACTIVE;

    public SavingsAccount() throws Exception{
        super();
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
    }

    public SavingsAccount(BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, BigDecimal minimumBalance,  Status status) throws Exception{
        super(balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        setInterestRate(interestRate);
        this.minimumBalance = minimumBalance;
        this.status = status;
    }

    public SavingsAccount(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, BigDecimal minimumBalance,  Status status) throws Exception{
        super(id, creationDate, nextDateForInterestPayment, balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        setInterestRate(interestRate);
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

    @Override
    public BigDecimal getInterestRate(){
        return this.interestRate;
    }

    @Override
    public void setInterestRate(BigDecimal interestRate) throws Exception{
        this.interestRate = interestRate;
    }

    @Override
    //If balance drops below minimumBalance then the penaltyFee is automatically deducted
    public void setBalance(BigDecimal newBalance){
        if(newBalance.compareTo(minimumBalance)<0){
            super.setBalance(newBalance.subtract(getPenaltyFee()));
        } else {
            super.setBalance(newBalance);
        }
    }

}
