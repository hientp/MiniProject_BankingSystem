package midterm.service;

import midterm.controller.dto.*;
import midterm.models.Address;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;
import midterm.models.users.Admin;
import midterm.models.users.FirstPartyUser;
import midterm.models.users.ThirdParty;
import midterm.repository.AccountRepository;
import midterm.repository.AddressRepository;
import midterm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    public AccountHolder createNewCustomer(FirstPartyUserDTO firstPartyUserDTO) {
        try {
            Address address= addressRepository.findById(firstPartyUserDTO.getAddressId()).get();
            AccountHolder newUser = new AccountHolder(firstPartyUserDTO.getName(),firstPartyUserDTO.getBirthDay(),address);
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided values not valid.");
        }
    }

    public Admin createNewAdmin(FirstPartyUserDTO firstPartyUserDTO) {
        try {
            Address address= addressRepository.findById(firstPartyUserDTO.getAddressId()).get();
            Admin newUser = new Admin(firstPartyUserDTO.getName(),firstPartyUserDTO.getBirthDay(),address);
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided values not valid.");
        }
    }

    public ThirdParty createNewThirdParty(ThirdPartyDTO thirdPartyDTO) {
        try {
            ThirdParty newUser = new ThirdParty(thirdPartyDTO.getName(),thirdPartyDTO.getHashedKey());
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided values not valid.");
        }
    }

}
