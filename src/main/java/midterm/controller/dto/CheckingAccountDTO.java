package midterm.controller.dto;

import midterm.models.TransactionPartners;
import midterm.models.users.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CheckingAccountDTO {

    @NotNull(message = "You must supply a primary Owner!")
    private Integer primaryOwnerId;

    private Integer secondaryOwnerId;
    private LocalDateTime creationDate;

    @NotNull
    private BigDecimal balance;
    @NotNull @NotEmpty
    private String secretKey;

    public CheckingAccountDTO() {
    }

    public CheckingAccountDTO(Integer primaryOwnerId, Integer secondaryOwnerId, LocalDateTime creationDate, BigDecimal balance, String secretKey) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.creationDate = creationDate;
        this.balance = balance;
        this.secretKey = secretKey;
    }

    public Integer getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwner(Integer primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Integer getSecondaryOwnerId() {
        return secondaryOwnerId;
    }

    public void setSecondaryOwner(Integer secondaryOwnerId) {
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
}
