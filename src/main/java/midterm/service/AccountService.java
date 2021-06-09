package midterm.service;

import midterm.controller.dto.CheckingAccountDTO;
import midterm.models.accounts.CheckingAccount;
import midterm.models.enums.Status;
import midterm.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public CheckingAccount createNewCheckingAccount(CheckingAccountDTO checkingAccountDTO) {
        try {
            CheckingAccount newCheckingAccount = new CheckingAccount(checkingAccountDTO.getBalance(),checkingAccountDTO.getSecretKey(),checkingAccountDTO.getPrimaryOwner(),checkingAccountDTO.getSecondaryOwner(), Status.ACTIVE);
            return accountRepository.save(newCheckingAccount);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided values not valid.");
        }
    }

}
