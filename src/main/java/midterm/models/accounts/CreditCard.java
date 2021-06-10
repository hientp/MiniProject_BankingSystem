package midterm.models.accounts;

import midterm.models.enums.Period;
import midterm.models.users.AccountHolder;
import midterm.models.users.FirstPartyUser;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "CreditCard")
@Table(name = "credit_card")
public class CreditCard extends Account {
    @DecimalMax(value = "100000", inclusive = true,message="A credtLimit more than 100000 is not allowed!")
    @DecimalMin(value = "100", inclusive = true,message="A creditLimit less than 100 is not allowed!")
    @NotNull
    private BigDecimal creditLimit = new BigDecimal("100");

    @DecimalMax(value = "0.2", inclusive = true,message="An interestRate more than 0.2 is not allowed!")
    @DecimalMin(value = "0.1", inclusive = true,message="An interestRate less than 0.1 is not allowed!")
    @NotNull
    private BigDecimal interestRate= new BigDecimal("0.2");

    public CreditCard() throws Exception{
        super();
        setInterestRatePaymentDate(getCreationDate().plusMonths(1));
    }

    public CreditCard(BigDecimal balance, String secretKey, FirstPartyUser primaryOwner, FirstPartyUser secondaryOwner, BigDecimal interestRate, BigDecimal creditLimit) throws Exception {
        super(balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        setInterestRatePaymentDate(getCreationDate().plusMonths(1));
        setInterestRate(interestRate);
        setBalance(balance);
        this.creditLimit = creditLimit;
    }

    public CreditCard(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, FirstPartyUser primaryOwner, FirstPartyUser secondaryOwner, BigDecimal interestRate, BigDecimal creditLimit) throws Exception{
        super(id, creationDate, nextDateForInterestPayment, balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        setInterestRate(interestRate);
        setBalance(balance);
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
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
    public void setBalance(BigDecimal balance) throws RuntimeException {
        if(balance.compareTo(this.getCreditLimit().multiply(new BigDecimal("-1")))<0){
            throw new RuntimeException("The credit limit for this account is "+ this.getCreditLimit()+" ! It is not allowed that the balance exceeds the limit!");
        } else {
            super.setBalance(balance);
        }
    }

    @Override
    public void changeBalance(BigDecimal valueToChange) throws RuntimeException{
        setBalance(getBalance().add(valueToChange));
    }

    @Override
    public void setCreationDate(LocalDateTime creationDate){
        super.setCreationDate(creationDate);
        super.setInterestRatePaymentDate(creationDate.plusMonths(1));
    }

    @Override
    public BigDecimal getBalance() throws RuntimeException{
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime lastInterestPaymentDate = this.getInterestRatePaymentDate();
        //Check if there needs to be one or more interest payments
        if(lastInterestPaymentDate.isBefore(currentTime)) {
            int years = (int) java.time.temporal.ChronoUnit.YEARS.between(lastInterestPaymentDate, currentTime);
            BigDecimal factor = (new BigDecimal("1").add(getInterestRate().divide(new BigDecimal("100")))).pow(years+1);
            //Interest is only charged, if balance is negative
            if(super.getBalance().compareTo(new BigDecimal("0"))<0){
                super.setBalance(super.getBalance().multiply(factor));
            }
            super.setNextInterestRatePaymentDate(Period.YEARLY,years+1);
        }
        return super.getBalance();
    }


}
