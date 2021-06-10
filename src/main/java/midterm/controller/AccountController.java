package midterm.controller;

import midterm.controller.dto.BalanceDTO;
import midterm.controller.dto.CheckingAccountDTO;
import midterm.controller.dto.CreditCardDTO;
import midterm.controller.dto.SavingsAccountDTO;
import midterm.models.accounts.Account;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.users.FirstPartyUser;
import midterm.repository.AccountRepository;
import midterm.repository.UserRepository;
import midterm.service.AccountService;
import midterm.service.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TestData testData;

    //Generiere Testdaten
    @PostMapping("/banking/testData")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void generateTestData(){
        testData.generateData();
    }

    //Erhalte CheckingAccount Informationen
    @GetMapping("/banking/checking_accounts/")
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccount> searchCheckingAccount(@RequestParam Optional<Integer> user) {
        if(user.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            return accountRepository.findCheckingAccountByPrimaryOwner(primaryOwner);
        } else {
            return accountRepository.findAllCheckingAccounts();
        }
    }

//    @GetMapping("/banking/checking_accounts/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<CheckingAccount> searchCheckingAccountByID(@PathVariable Integer userId) {
//            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(userId).get();
//            return accountRepository.findCheckingAccountByPrimaryOwner(primaryOwner);
//    }


    //Erhalte SavingsAccount Informationen
    @GetMapping("/banking/savings_accounts/")
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> searchSavingsAccount(@RequestParam Optional<Integer> user) {
        if(user.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            return accountRepository.findSavingsAccountByPrimaryOwner(primaryOwner);
        } else {
            return accountRepository.findAllSavingsAccounts();
        }
    }

    //Erhalte CreditCard Informationen
    @GetMapping("/banking/credit_cards/")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> searchCreditCard(@RequestParam Optional<Integer> user) {
        if(user.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            return accountRepository.findCreditCardByPrimaryOwner(primaryOwner);
        } else {
            return accountRepository.findAllCreditCards();
        }
    }

    //Erhalte Balance Informationen
    @GetMapping("/banking/account_balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceDTO searchBalance(@PathVariable Integer id) {
        return new BalanceDTO(accountRepository.findBalanceOfAccount(id));
    }

    //Modify balance checking account
    @PatchMapping("/banking/checking_account_balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CheckingAccount modifyCheckingAccountBalance(@PathVariable Integer id, @RequestBody @Valid BalanceDTO balanceDTO)  {
         return accountService.modifyCheckingAccountBalance(id,balanceDTO);
    }

    //Modify balance savings account
    @PatchMapping("/banking/savings_account_balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SavingsAccount modifySavingsAccountBalance(@PathVariable Integer id, @RequestBody @Valid BalanceDTO balanceDTO)  {
        return accountService.modifySavingsAccountBalance(id,balanceDTO);
    }

    //Modify balance credit card
    @PatchMapping("/banking/credit_card_balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard modifyCreditCardBalance(@PathVariable Integer id, @RequestBody @Valid BalanceDTO balanceDTO)  {
        return accountService.modifyCreditCardBalance(id,balanceDTO);
    }


    //Anlegen von neuem checkingAccount
    @PostMapping("/banking/account/new_checking_account/")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingAccount createNewCheckingAccount(@RequestBody @Valid CheckingAccountDTO checkingAccountDTO) {
        return accountService.createNewCheckingAccount(checkingAccountDTO);
    }

    //Anlegen von neuem savingsaccount
    @PostMapping("/banking/account/new_savings_account/")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsAccount createNewSavingsAccount(@RequestBody @Valid SavingsAccountDTO savingsAccountDTO) {
        return accountService.createNewSavingsAccount(savingsAccountDTO);
    }

    //Anlegen von neuer creditcard
    @PostMapping("/banking/account/new_credit_card/")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard createNewCreditCard(@RequestBody @Valid CreditCardDTO creditCardDTO) {
        return accountService.createNewCreditCard(creditCardDTO);
    }


}
