package midterm.controller;

import midterm.controller.dto.CheckingAccountDTO;
import midterm.models.accounts.Account;
import midterm.models.accounts.CheckingAccount;
import midterm.repository.AccountRepository;
import midterm.service.AccountService;
import midterm.service.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

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
    //FIXME endlosschleife addresse - primaryuser
    @GetMapping("/banking/checking_accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccount> search(@RequestParam Optional<Integer> primaryOwner) {
        if(primaryOwner.isPresent()) {
//            return accountRepository.findCheckingAccountByPrimaryOwnerID(primaryOwner.get());
            return accountRepository.findAllCheckingAccounts();
        } else {
            return accountRepository.findAllCheckingAccounts();
        }
    }

    //Anlegen von neuem checkingAccount
    @PostMapping("/banking/account/new_checking_account/")
    @ResponseStatus(HttpStatus.OK)
    public CheckingAccount createNewCheckingAccount(@RequestBody @Valid CheckingAccountDTO checkingAccountDTO) {
        return accountService.createNewCheckingAccount(checkingAccountDTO);
    }


}
