package midterm.controller;

import midterm.controller.dto.*;
import midterm.models.Transaction;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.users.FirstPartyUser;
import midterm.repository.AccountRepository;
import midterm.repository.TransactionRepository;
import midterm.repository.UserRepository;
import midterm.service.AccountService;
import midterm.service.TestData;
import midterm.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;


    //Anlegen von neuem checkingAccount
    @PostMapping("/banking/transferMoney/")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction transferMoney(@RequestBody @Valid TransactionDTO transactionDTO) {
        return transactionService.transferMoney(transactionDTO);
    }




}
