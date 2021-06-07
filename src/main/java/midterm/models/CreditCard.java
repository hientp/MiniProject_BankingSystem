package midterm.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "CreditCard")
@Table(name = "credit_card")
public class CreditCard extends Account {
    private BigDecimal creditLimit = new BigDecimal("100");

    public CreditCard() throws Exception{
        super();
        setInterestRatePaymentDate(getCreationDate().plusMonths(1));
        setInterestRate(new BigDecimal("0.2"));
    }

    public CreditCard(BigDecimal balance, String secretKey, String primaryOwner, String secondaryOwner, BigDecimal interestRate,BigDecimal creditLimit) {
        super(balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        setInterestRatePaymentDate(getCreationDate().plusMonths(1));
        this.creditLimit = creditLimit;
    }

    public CreditCard(Integer id, LocalDateTime creationDate, LocalDateTime nextDateForInterestPayment, BigDecimal balance, String secretKey, String primaryOwner, String secondaryOwner, BigDecimal interestRate, BigDecimal creditLimit) {
        super(id, creationDate, nextDateForInterestPayment, balance, secretKey, primaryOwner, secondaryOwner, interestRate);
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
}
