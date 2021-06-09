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
    private AccountHolder primaryOwner;

    private AccountHolder secondaryOwner;
    private LocalDateTime creationDate;

    @NotNull
    private BigDecimal balance;
    @NotNull @NotEmpty
    private String secretKey;

    public CheckingAccountDTO() {
    }

    public CheckingAccountDTO(AccountHolder primaryOwner, AccountHolder secondaryOwner, LocalDateTime creationDate, BigDecimal balance, String secretKey) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creationDate = creationDate;
        this.balance = balance;
        this.secretKey = secretKey;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
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
