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
        this.status = status;
    }

    public CheckingAccount(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner,  Status status) throws Exception{
        super(id, creationDate, nextDateForInterestPayment, balance, secretKey, primaryOwner, secondaryOwner, new BigDecimal("0")); //test
        setInterestRate(interestRate);
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

    @Override
    public BigDecimal getInterestRate(){
        return this.interestRate;
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