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
import java.util.ArrayList;
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
    @RequestMapping(value = "/banking/checking_accounts/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccountDTO> searchCheckingAccount(@RequestBody Optional<Integer> user) {
        if (user.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            List<CheckingAccount> checkingAccountList = accountRepository.findCheckingAccountByPrimaryOwner(primaryOwner);

            List<CheckingAccountDTO> checkingAccountDTOList = new ArrayList<>();
            for (int x = 0; x < checkingAccountList.size(); x++) {
                CheckingAccount checkingAccount = checkingAccountList.get(x);
                checkingAccountDTOList.add(new CheckingAccountDTO(checkingAccount.getPrimaryOwner().getId(), null, checkingAccount.getCreationDate(), checkingAccount.getBalance(), checkingAccount.getSecretKey()));
            }

            return checkingAccountDTOList;
        } else {
            List<CheckingAccount> checkingAccountList = accountRepository.findAllCheckingAccounts();

            List<CheckingAccountDTO> checkingAccountDTOList = new ArrayList<>();
            for (int x = 0; x < checkingAccountList.size(); x++) {
                CheckingAccount checkingAccount = checkingAccountList.get(x);
                checkingAccountDTOList.add(new CheckingAccountDTO(checkingAccount.getPrimaryOwner().getId(), null, checkingAccount.getCreationDate(), checkingAccount.getBalance(), checkingAccount.getSecretKey()));

            }
            return checkingAccountDTOList;
        }

    }
//    @GetMapping("/banking/checking_accounts/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<CheckingAccount> searchCheckingAccountByID(@PathVariable Integer userId) {
//            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(userId).get();
//            return accountRepository.findCheckingAccountByPrimaryOwner(primaryOwner);
//    }


    @RequestMapping(value = "/banking/savings_accounts/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccountDTO> searchSavingsAccount(@RequestBody Optional<Integer> user) {
        if (user.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            List<SavingsAccount> savingsAccountList = accountRepository.findSavingsAccountByPrimaryOwner(primaryOwner);

            List<SavingsAccountDTO> savingsAccountDTOList = new ArrayList<>();
            for (int x = 0; x < savingsAccountDTOList.size(); x++) {
                SavingsAccount savingsAccount = savingsAccountList.get(x);
                savingsAccountDTOList.add(new SavingsAccountDTO(savingsAccount.getPrimaryOwner().getId(), null, savingsAccount.getCreationDate(), savingsAccount.getBalance(), savingsAccount.getSecretKey(),savingsAccount.getMinimumBalance(),savingsAccount.getInterestRate()));
            }

            return savingsAccountDTOList;
        } else {
            List<SavingsAccount> savingsAccountList = accountRepository.findAllSavingsAccounts();

            List<SavingsAccountDTO> savingsAccountDTOList = new ArrayList<>();
            for (int x = 0; x < savingsAccountDTOList.size(); x++) {
                SavingsAccount savingsAccount = savingsAccountList.get(x);
                savingsAccountDTOList.add(new SavingsAccountDTO(savingsAccount.getPrimaryOwner().getId(), null, savingsAccount.getCreationDate(), savingsAccount.getBalance(), savingsAccount.getSecretKey(),savingsAccount.getMinimumBalance(),savingsAccount.getInterestRate()));
            }
            return savingsAccountDTOList;
        }
    }

    //Erhalte CreditCard Informationen
   @RequestMapping(value = "/banking/credit_cards/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCardDTO> searchCreditCard(@RequestBody Optional<Integer> user) {
        if (user.isPresent()) {
            FirstPartyUser primaryOwner = (FirstPartyUser) userRepository.findById(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            List<CreditCard> creditCardList = accountRepository.findCreditCardByPrimaryOwner(primaryOwner);

            List<CreditCardDTO> creditCardDTOList = new ArrayList<>();
            for (int x = 0; x < creditCardDTOList.size(); x++) {
                CreditCard creditCard = creditCardList.get(x);
                creditCardDTOList.add(new CreditCardDTO(creditCard.getPrimaryOwner().getId(), null, creditCard.getCreationDate(), creditCard.getBalance(), creditCard.getSecretKey(),creditCard.getCreditLimit(),creditCard.getInterestRate()));
            }

            return creditCardDTOList;
        } else {
            List<CreditCard> creditCardList = accountRepository.findAllCreditCards();

            List<CreditCardDTO> creditCardDTOList = new ArrayList<>();
            for (int x = 0; x < creditCardDTOList.size(); x++) {
                CreditCard creditCard = creditCardList.get(x);
                creditCardDTOList.add(new CreditCardDTO(creditCard.getPrimaryOwner().getId(), null, creditCard.getCreationDate(), creditCard.getBalance(), creditCard.getSecretKey(),creditCard.getCreditLimit(),creditCard.getInterestRate()));
            }

            return creditCardDTOList;
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
