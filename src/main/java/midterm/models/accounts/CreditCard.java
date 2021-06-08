package midterm.models.accounts;

import midterm.models.users.AccountHolder;

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

    public CreditCard(BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate,BigDecimal creditLimit) throws Exception {
        super(balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        setInterestRatePaymentDate(getCreationDate().plusMonths(1));
        setInterestRate(interestRate);
        setBalance(balance);
        this.creditLimit = creditLimit;
    }

    public CreditCard(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, BigDecimal creditLimit) throws Exception{
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
    public void setBalance(BigDecimal balance) throws Exception {
        if(balance.compareTo(this.getCreditLimit().multiply(new BigDecimal("-1")))<0){
            throw new Exception("The credit limit for this account is "+ this.getCreditLimit()+" ! It is not allowed that the balance exceeds the limit!");
        } else {
            super.setBalance(balance);
        }
    }

    @Override
    public void changeBalance(BigDecimal valueToChange) throws Exception{
        setBalance(getBalance().add(valueToChange));
    }
}
