package midterm.controller.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SavingsAccountDTO {

    @NotNull(message = "You must supply a primary Owner!")
    private Integer primaryOwnerId;

    private Integer secondaryOwnerId;
    private LocalDateTime creationDate;

    @NotNull
    private BigDecimal balance;
    @NotNull @NotEmpty
    private String secretKey;

    @DecimalMin(value = "100.0", inclusive = true,message="A MinumumBalance less than 100 is not allowed!")
    @DecimalMax(value = "1000.0", inclusive = true,message="A MinumumBalance more than 1000 is not allowed!")
    @NotNull
    private BigDecimal minimumBalance= new BigDecimal("1000");

    @DecimalMax(value = "0.5", inclusive = true,message="An interestRate more than 0.5 is not allowed!")
    @NotNull
    private BigDecimal interestRate= new BigDecimal("0.0025");

    public SavingsAccountDTO(Integer primaryOwnerId, Integer secondaryOwnerId, LocalDateTime creationDate, BigDecimal balance, String secretKey, BigDecimal minimumBalance, BigDecimal interestRate) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.creationDate = creationDate;
        this.balance = balance;
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccountDTO() {
    }

    public Integer getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(Integer primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Integer getSecondaryOwnerId() {
        return secondaryOwnerId;
    }

    public void setSecondaryOwnerId(Integer secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
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

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
