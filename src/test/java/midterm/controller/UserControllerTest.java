package midterm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import midterm.controller.dto.CheckingAccountDTO;
import midterm.controller.dto.FirstPartyUserDTO;
import midterm.controller.dto.ThirdPartyDTO;
import midterm.models.Address;
import midterm.models.Transaction;
import midterm.models.TransactionPartners;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;
import midterm.models.users.Admin;
import midterm.models.users.ThirdParty;
import midterm.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = "patrick")
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionPartnersRepository transactionPartnersRepository;

    //Init class instances
    List<Address> addressList = new ArrayList<Address>();
    List<AccountHolder> accountHolderList = new ArrayList<AccountHolder>();
    List<Admin> adminList = new ArrayList<Admin>();
    List<ThirdParty> thirdPartyList = new ArrayList<ThirdParty>();

    @BeforeAll
    public void setUp() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        for(int i=0;i<40;i++) {
            addressList.add(new Address("Default Str. "+i, "Berlin", "Germany", "15x"+i));
            addressRepository.save(addressList.get(i));
        }
        //setup accountholders
        int k=0;
        for(int i=0;i<60;i++) {
            if(i==40){k=0;}
            accountHolderList.add(new AccountHolder("Test_Customer_" + i, new Date(70, 5, 20), addressList.get(k)));
            k++;
            userRepository.save(accountHolderList.get(i));
        }
        //setup admins & thirdparty users
        for(int i=0;i<3;i++) {
            adminList.add(new Admin("Test_Admin_"+i,new Date(90, 4, 9),addressList.get(i)));
            thirdPartyList.add( new ThirdParty("Third_Party_"+i,new UUID(10,2)));
            userRepository.save(adminList.get(i));
            userRepository.save(thirdPartyList.get(i));
        }
    }


    @Test
    void searchUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/banking/user/")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Test_Customer_1"));
        assertTrue(result.getResponse().getContentAsString().contains("Test_Admin_0"));
        assertTrue(result.getResponse().getContentAsString().contains("Third_Party_0"));

        result = mockMvc.perform(get("/banking/user/?user=1")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Test_Customer_0"));

        mockMvc.perform(get("/banking/user/?user=100")).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void createNewCustomer() throws Exception {
        FirstPartyUserDTO firstPartyUserDTO = new FirstPartyUserDTO();
        firstPartyUserDTO.setName("Peter Paul");
        firstPartyUserDTO.setBirthDay(new Date(90, 5, 20));
        firstPartyUserDTO.setAddressId(1);


        String jsonString = objectMapper.writeValueAsString(firstPartyUserDTO);
        ResultActions checkMock =mockMvc.perform(post("/banking/user/new_account_holder/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void createNewAdmin() throws Exception {
        FirstPartyUserDTO firstPartyUserDTO = new FirstPartyUserDTO();
        firstPartyUserDTO.setName("Peter Paul");
        firstPartyUserDTO.setBirthDay(new Date(90, 5, 20));
        firstPartyUserDTO.setAddressId(1);


        String jsonString = objectMapper.writeValueAsString(firstPartyUserDTO);
        ResultActions checkMock =mockMvc.perform(post("/banking/user/new_admin/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void createNewThirdParty() throws Exception {
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO();
        thirdPartyDTO.setName("Peter Paul");
        thirdPartyDTO.setHashedKey(new UUID(2,10));

        String jsonString = objectMapper.writeValueAsString(thirdPartyDTO);
        ResultActions checkMock =mockMvc.perform(post("/banking/user/new_third_party/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}