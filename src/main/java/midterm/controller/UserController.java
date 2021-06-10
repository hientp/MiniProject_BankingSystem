package midterm.controller;

import midterm.controller.dto.*;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.users.*;
import midterm.repository.AccountRepository;
import midterm.repository.UserRepository;
import midterm.service.AccountService;
import midterm.service.TestData;
import midterm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    //Erhalte FirstPartyUser Informationen
    @GetMapping("/banking/user/")
    @ResponseStatus(HttpStatus.OK)
    public List<User> searchUser(@RequestParam Optional<Integer> user) {
        if(user.isPresent()) {
            User primaryOwner = userRepository.findById(user.get()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            List<User> userList = new ArrayList<>();
            userList.add(primaryOwner);
            return userList;
        } else {
            return userRepository.findAll();
        }
    }


    //Anlegen von neuem customer
    @PostMapping("/banking/user/new_account_holder/")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createNewCustomer(@RequestBody @Valid FirstPartyUserDTO firstPartyUserDTO) {
        return userService.createNewCustomer(firstPartyUserDTO);
    }

    //Anlegen von neuem admin
    @PostMapping("/banking/user/new_admin/")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin createNewAdmin(@RequestBody @Valid FirstPartyUserDTO firstPartyUserDTO) {
        return userService.createNewAdmin(firstPartyUserDTO);
    }

    //Anlegen von neuem thirdpartyaccount
    @PostMapping("/banking/user/new_third_party/")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createNewThirdParty(@RequestBody @Valid ThirdPartyDTO thirdPartyDTO) {
        return userService.createNewThirdParty(thirdPartyDTO);
    }

}
