package midterm.service;

import midterm.controller.dto.CheckingAccountDTO;
import midterm.models.accounts.CheckingAccount;
import midterm.models.enums.Status;
import midterm.models.users.FirstPartyUser;
import midterm.repository.AccountRepository;
import midterm.repository.UserRepository;
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
    @Autowired
    UserRepository userRepository;

    public CheckingAccount createNewCheckingAccount(CheckingAccountDTO checkingAccountDTO) {
        try {
            FirstPartyUser PrimaryOwner= (FirstPartyUser) userRepository.findById(checkingAccountDTO.getPrimaryOwnerId()).get();
            //FirstPartyUser SecondaryOwner= (FirstPartyUser) userRepository.findById(checkingAccountDTO.getSecondaryOwnerId()).get();
            CheckingAccount newCheckingAccount = new CheckingAccount(checkingAccountDTO.getBalance(),checkingAccountDTO.getSecretKey(),PrimaryOwner,null, Status.ACTIVE);
            return accountRepository.save(newCheckingAccount);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided values not valid.");
        }
    }

}
