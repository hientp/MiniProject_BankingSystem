package midterm.controller.dto;

import midterm.models.accounts.CheckingAccount;
import midterm.models.users.AccountHolder;
import midterm.repository.AccountRepository;
import midterm.service.AccountService;
import midterm.service.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BalanceDTO {

    @NotNull(message = "You must supply a balance!")
    private BigDecimal balance;

    public BalanceDTO() {
    }

    public BalanceDTO(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
