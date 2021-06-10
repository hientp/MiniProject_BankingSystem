package midterm.controller;

import midterm.controller.dto.BalanceDTO;
import midterm.controller.dto.CheckingAccountDTO;
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
    public List<CheckingAccount> searchCheckingAccount(@RequestParam Optional<Integer> userId) {
        if(userId.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(userId.get()).get();
            return accountRepository.findCheckingAccountByPrimaryOwner(primaryOwner);
        } else {
            return accountRepository.findAllCheckingAccounts();
        }
    }

    //Erhalte SavingsAccount Informationen
    @GetMapping("/banking/savings_accounts/")
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> searchSavingsAccount(@RequestParam Optional<Integer> userId) {
        if(userId.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(userId.get()).get();
            return accountRepository.findSavingsAccountByPrimaryOwner(primaryOwner);
        } else {
            return accountRepository.findAllSavingsAccounts();
        }
    }

    //Erhalte CreditCard Informationen
    @GetMapping("/banking/credit_cards/")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> searchCreditCard(@RequestParam Optional<Integer> userId) {
        if(userId.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(userId.get()).get();
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

    //Anlegen von neuem checkingAccount
    @PostMapping("/banking/account/new_checking_account/")
    @ResponseStatus(HttpStatus.OK)
    public CheckingAccount createNewCheckingAccount(@RequestBody @Valid CheckingAccountDTO checkingAccountDTO) {
        return accountService.createNewCheckingAccount(checkingAccountDTO);
    }


}
