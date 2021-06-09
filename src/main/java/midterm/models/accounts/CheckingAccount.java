package midterm.models.accounts;

import midterm.models.enums.Period;
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
    private BigDecimal interestRate= new BigDecimal("0");

    @Enumerated(EnumType.STRING)
    private Status status= Status.ACTIVE;

    public CheckingAccount() throws Exception{
        super();
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
    }

    public CheckingAccount(BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, Status status) throws Exception{
        super(balance, secretKey, primaryOwner, secondaryOwner, new BigDecimal("0"));
        setInterestRatePaymentDate(getCreationDate().plusYears(1));
        setInterestRate(interestRate);
        setBalance(balance);
        this.minimumBalance= new BigDecimal("250");
        this.status = status;
    }

    public CheckingAccount(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner,  Status status) throws Exception{
        super(id, creationDate, nextDateForInterestPayment, balance, secretKey, primaryOwner, secondaryOwner, new BigDecimal("0")); //test
        setInterestRate(interestRate);
        setBalance(balance);
        this.minimumBalance= new BigDecimal("250");
        this.status = status;
    }

    public BigDecimal getMinimumBalance() {
        return this.minimumBalance;
    }


    public BigDecimal getMonthlyMaintenanceFee() {
        return this.monthlyMaintenanceFee;
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

    @Override
    public BigDecimal getInterestRate(){
        return this.interestRate;
    }

    @Override
    //If balance drops below minimumBalance then the penaltyFee is automatically deducted
    //Balance can't drop below zero
    public void setBalance(BigDecimal newBalance) throws Exception{
        //Check if newBalance would be greater or equal zero
        if(newBalance.compareTo(new BigDecimal("0"))>=0){
            //Check if minimumBalance is reached
            if(newBalance.compareTo(this.minimumBalance)<0){
                super.setBalance(newBalance.subtract(getPenaltyFee()));
            } else {
                super.setBalance(newBalance);
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
