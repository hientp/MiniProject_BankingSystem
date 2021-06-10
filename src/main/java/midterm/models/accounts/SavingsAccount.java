package midterm.models.accounts;

import midterm.models.enums.Period;
import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;
import midterm.models.users.FirstPartyUser;

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

    private Boolean isMinimumBalanceReached = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    private Status status= Status.ACTIVE;

    public SavingsAccount() throws Exception{
        super();
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
    }

    public SavingsAccount(BigDecimal balance, String secretKey, FirstPartyUser primaryOwner, FirstPartyUser secondaryOwner, BigDecimal interestRate, BigDecimal minimumBalance, Status status) throws Exception{
        super(balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        setInterestRate(interestRate);
        setBalance(balance);
        this.minimumBalance = minimumBalance;
        this.status = status;
    }

    public SavingsAccount(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, FirstPartyUser primaryOwner, FirstPartyUser secondaryOwner, BigDecimal interestRate, BigDecimal minimumBalance,  Status status) throws Exception{
        super(id, creationDate, nextDateForInterestPayment, balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        setInterestRate(interestRate);
        setBalance(balance);
        this.minimumBalance = minimumBalance;
        this.status = status;
    }

    public Boolean getMinimumBalanceReached() {
        return isMinimumBalanceReached;
    }

    public void setMinimumBalanceReached(Boolean minimumBalanceReached) {
        isMinimumBalanceReached = minimumBalanceReached;
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
    //Balance can't drop below zero
    public void setBalance(BigDecimal newBalance) throws Exception{
        //Check if newBalance would be greater or equal zero
        if(newBalance.compareTo(new BigDecimal("0"))>=0){
            //Check if minimumBalance is reached
            if(!isMinimumBalanceReached) {
                //unless the minimumBalance isn't reached, check if would now drop under the minimumBalance
                if (newBalance.compareTo(this.minimumBalance) < 0) {
                    super.setBalance(newBalance.subtract(getPenaltyFee()));
                    setMinimumBalanceReached(Boolean.TRUE);
                } else {
                    super.setBalance(newBalance);
                }
            } else { //Case if MinimumBalance is already reached
                if (newBalance.compareTo(this.minimumBalance) >= 0) {
                    super.setBalance(newBalance);
                    setMinimumBalanceReached(Boolean.FALSE);
                } else {
                    super.setBalance(newBalance);
                }
            }
        } else {
            throw new Exception("The balance of this account would drop below 0! Transaction denied.");
        }
    }

    @Override
    public void changeBalance(BigDecimal valueToChange) throws Exception{
        setBalance(getBalance().add(valueToChange));
    }

    @Override
    public void setCreationDate(LocalDateTime creationDate){
        super.setCreationDate(creationDate);
        super.setInterestRatePaymentDate(creationDate.plusYears(1));
    }

    @Override
    public BigDecimal getBalance() throws Exception{
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime lastInterestPaymentDate = this.getInterestRatePaymentDate();
        //Check if there needs to be one or more interest payments
        if(lastInterestPaymentDate.isBefore(currentTime)) {
            int years = (int) java.time.temporal.ChronoUnit.YEARS.between(lastInterestPaymentDate, currentTime);
            BigDecimal factor = (new BigDecimal("1").add(getInterestRate().divide(new BigDecimal("100")))).pow(years+1);
            super.setBalance(super.getBalance().multiply(factor));
            super.setNextInterestRatePaymentDate(Period.YEARLY,years+1);
        }
        return super.getBalance();
    }


}
