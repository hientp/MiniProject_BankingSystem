package midterm.controller.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreditCardDTO {

    @NotNull(message = "You must supply a primary Owner!")
    private Integer primaryOwnerId;

    private Integer secondaryOwnerId;
    private LocalDateTime creationDate;

    @NotNull
    private BigDecimal balance;
    @NotNull @NotEmpty
    private String secretKey;

    @DecimalMax(value = "100000", inclusive = true,message="A credtLimit more than 100000 is not allowed!")
    @DecimalMin(value = "100", inclusive = true,message="A creditLimit less than 100 is not allowed!")
    @NotNull
    private BigDecimal creditLimit = new BigDecimal("100");

    @DecimalMax(value = "0.2", inclusive = true,message="An interestRate more than 0.2 is not allowed!")
    @DecimalMin(value = "0.1", inclusive = true,message="An interestRate less than 0.1 is not allowed!")
    @NotNull
    private BigDecimal interestRate= new BigDecimal("0.2");


    public CreditCardDTO() {
    }

    public CreditCardDTO(Integer primaryOwnerId, Integer secondaryOwnerId, LocalDateTime creationDate, BigDecimal balance, String secretKey, BigDecimal creditLimit, BigDecimal interestRate) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.creationDate = creationDate;
        this.balance = balance;
        this.secretKey = secretKey;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public void setPrimaryOwnerId(Integer primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public void setSecondaryOwnerId(Integer secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getPrimaryOwnerId() {
        return primaryOwnerId;
    }


    public Integer getSecondaryOwnerId() {
        return secondaryOwnerId;
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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


}
