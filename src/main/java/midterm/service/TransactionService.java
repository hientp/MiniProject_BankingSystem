package midterm.service;

import midterm.Utils;
import midterm.controller.dto.*;
import midterm.models.Transaction;
import midterm.models.accounts.Account;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
import midterm.models.users.FirstPartyUser;
import midterm.repository.AccountRepository;
import midterm.repository.TransactionPartnersRepository;
import midterm.repository.TransactionRepository;
import midterm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionPartnersRepository transactionPartnersRepository;
    @Autowired
    UserRepository userRepository;

    public Transaction transferMoney(TransactionDTO transactionDTO) {
        try {
            BigDecimal amount = transactionDTO.getAmount();
            Integer sender_id = transactionDTO.getSenderAccountId();
            Integer receiver_id = transactionDTO.getReceiverAccountId();
            Account sender = Utils.findAccountById(sender_id,accountRepository);
            Account receiver = Utils.findAccountById(receiver_id,accountRepository);
            Utils.transactMoneySecured(accountRepository,sender,receiver,transactionRepository,transactionPartnersRepository,amount);
            return null;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test");
        }
    }

}
