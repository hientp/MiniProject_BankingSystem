package midterm.service;

import midterm.controller.dto.BalanceDTO;
import midterm.controller.dto.CheckingAccountDTO;
import midterm.controller.dto.CreditCardDTO;
import midterm.controller.dto.SavingsAccountDTO;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
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

    public SavingsAccount createNewSavingsAccount(SavingsAccountDTO savingsAccountDTO) {
        try {
            FirstPartyUser PrimaryOwner= (FirstPartyUser) userRepository.findById(savingsAccountDTO.getPrimaryOwnerId()).get();
            //FirstPartyUser SecondaryOwner= (FirstPartyUser) userRepository.findById(checkingAccountDTO.getSecondaryOwnerId()).get();
            SavingsAccount newSavingsAccount = new SavingsAccount(savingsAccountDTO.getBalance(),savingsAccountDTO.getSecretKey(),PrimaryOwner,null,savingsAccountDTO.getInterestRate(),savingsAccountDTO.getMinimumBalance(),Status.ACTIVE);
            return accountRepository.save(newSavingsAccount);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided values not valid.");
        }
    }

    public CreditCard createNewCreditCard(CreditCardDTO creditCardDTO) {
        try {
            FirstPartyUser PrimaryOwner= (FirstPartyUser) userRepository.findById(creditCardDTO.getPrimaryOwnerId()).get();
            //FirstPartyUser SecondaryOwner= (FirstPartyUser) userRepository.findById(checkingAccountDTO.getSecondaryOwnerId()).get();
            CreditCard newCreditCard = new CreditCard(creditCardDTO.getBalance(),creditCardDTO.getSecretKey(),PrimaryOwner,null,creditCardDTO.getInterestRate(),creditCardDTO.getCreditLimit());
            return accountRepository.save(newCreditCard);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided values not valid.");
        }
    }

    public CheckingAccount modifyCheckingAccountBalance (Integer id, BalanceDTO balanceDTO) {
        try{
            CheckingAccount account = accountRepository.findCheckingAccountbyId(id);
            account.setBalance(balanceDTO.getBalance());
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something happened.");
        }
    }

    public SavingsAccount modifySavingsAccountBalance (Integer id, BalanceDTO balanceDTO) {
        try{
            SavingsAccount account = accountRepository.findSavingsAccountbyId(id);
            account.setBalance(balanceDTO.getBalance());
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something happened.");
        }
    }

    public CreditCard modifyCreditCardBalance (Integer id, BalanceDTO balanceDTO) {
        try{
            CreditCard account = accountRepository.findCreditCardbyId(id);
            account.setBalance(balanceDTO.getBalance());
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something happened.");
        }
    }

}
