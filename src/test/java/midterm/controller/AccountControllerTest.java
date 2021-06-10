package midterm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import midterm.controller.dto.BalanceDTO;
import midterm.controller.dto.CheckingAccountDTO;
import midterm.controller.dto.CreditCardDTO;
import midterm.controller.dto.SavingsAccountDTO;
import midterm.models.Address;
import midterm.models.accounts.CheckingAccount;
import midterm.models.accounts.CreditCard;
import midterm.models.accounts.SavingsAccount;
import midterm.models.enums.Status;
import midterm.models.users.AccountHolder;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = "patrick")
class AccountControllerTest {

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

    Address address1,address2;
    AccountHolder a1,a2,a3;
    CheckingAccount CA_1,CA_2,CA_3;
    SavingsAccount SA_1, SA_2, SA_3;
    CreditCard CC_1, CC_2, CC_3;

    @BeforeAll
    public void setUp() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //Init addresses
        address1= new Address("Default Str.1","Berlin","Germany","15xxx");
        address2= new Address("Default Str.2","Berlin","Germany","15xxx");
        a1 = new AccountHolder("Anton Alligator",new Date(100,5,10),address1);
        CA_1 = new CheckingAccount(new BigDecimal("2000"),"secretKey123",a1,null, Status.ACTIVE);
        SA_1 = new SavingsAccount( new BigDecimal("10000"),"banane123",a1,null,new BigDecimal("0.003"),new BigDecimal("1000"),Status.ACTIVE);
        CC_1= new CreditCard( new BigDecimal("3000"),"obst321",a1,null,new BigDecimal("0.2"),new BigDecimal("200"));

        addressRepository.save(address1);
        userRepository.save(a1);
        accountRepository.save(CA_1);
        accountRepository.save(SA_1);
        accountRepository.save(CC_1);

        a2 = new AccountHolder("Bernd Babyelefant",new Date(100,5,10),address2);
        CA_2 = new CheckingAccount(new BigDecimal("1000"),"secretKey456",a2,null, Status.ACTIVE);
        SA_2 = new SavingsAccount( new BigDecimal("20000"),"banane456",a2,null,new BigDecimal("0.001"),new BigDecimal("1000"),Status.ACTIVE);
        CC_2= new CreditCard( new BigDecimal("1000"),"obst654",a2,null,new BigDecimal("0.1"),new BigDecimal("300"));

        addressRepository.save(address2);
        userRepository.save(a2);
        accountRepository.save(CA_2);
        accountRepository.save(SA_2);
        accountRepository.save(CC_2);

        a3 = new AccountHolder("Chrissy Chamäleon",new Date(900,2,10),address1);
        CA_3 = new CheckingAccount(new BigDecimal("20000"),"secretKey789",a3,null, Status.ACTIVE);
        SA_3 = new SavingsAccount( new BigDecimal("100000"),"banane789",a3,null,new BigDecimal("0.002"),new BigDecimal("500"),Status.ACTIVE);
        CC_3= new CreditCard( new BigDecimal("6000"),"obst987",a3,null,new BigDecimal("0.2"),new BigDecimal("400"));

        userRepository.save(a3);
        accountRepository.save(CA_3);
        accountRepository.save(SA_3);
        accountRepository.save(CC_3);
    }


    @Test
    void searchCheckingAccount() throws Exception {
        MvcResult result = mockMvc.perform(get("/banking/checking_accounts/")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("secretKey123"));
        assertTrue(result.getResponse().getContentAsString().contains("secretKey456"));
        assertTrue(result.getResponse().getContentAsString().contains("secretKey789"));

        result = mockMvc.perform(get("/banking/checking_accounts/?user=1")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("secretKey123"));

        mockMvc.perform(get("/banking/checking_accounts/?user=99")).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void searchSavingsAccount() throws Exception {
        MvcResult result = mockMvc.perform(get("/banking/savings_accounts/")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("banane123"));
        assertTrue(result.getResponse().getContentAsString().contains("banane456"));
        assertTrue(result.getResponse().getContentAsString().contains("banane789"));

        result = mockMvc.perform(get("/banking/savings_accounts/?user=5")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("banane456"));

        mockMvc.perform(get("/banking/savings_accounts/?user=99")).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void searchCreditCard() throws Exception {
        MvcResult result = mockMvc.perform(get("/banking/credit_cards/")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("obst321"));
        assertTrue(result.getResponse().getContentAsString().contains("obst654"));
        assertTrue(result.getResponse().getContentAsString().contains("obst987"));

        result = mockMvc.perform(get("/banking/credit_cards/?user=9")).andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("obst987"));

        mockMvc.perform(get("/banking/credit_cards/?user=99")).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void searchBalance() throws Exception {
        ResultActions balanceMock = mockMvc.perform(get("/banking/account_balance/8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String jsonOutput = balanceMock.andReturn().getResponse().getContentAsString();

        BalanceDTO balanceDTO= objectMapper.readValue(jsonOutput, BalanceDTO.class);

        assertEquals(new BigDecimal("1000.00"),balanceDTO.getBalance());

    }

    @Test
    void modifyCheckingAccountBalance() throws Exception {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setBalance(new BigDecimal("999999"));
        String body = objectMapper.writeValueAsString(balanceDTO);
        mockMvc.perform(patch("/banking/checking_account_balance/2").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        assertEquals(accountRepository.findCheckingAccountbyId(2).getBalance(), new BigDecimal("999999.00"));
    }

    @Test
    void modifySavingsAccountBalance() throws Exception {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setBalance(new BigDecimal("999999"));
        String body = objectMapper.writeValueAsString(balanceDTO);
        mockMvc.perform(patch("/banking/savings_account_balance/3").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        assertEquals(accountRepository.findSavingsAccountbyId(3).getBalance(), new BigDecimal("999999.00"));
    }

    @Test
    void modifyCreditCardBalance() throws Exception {
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setBalance(new BigDecimal("999999"));
        String body = objectMapper.writeValueAsString(balanceDTO);
        mockMvc.perform(patch("/banking/credit_card_balance/4").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        assertEquals(accountRepository.findCreditCardbyId(4).getBalance(), new BigDecimal("999999.00"));
    }

    @Test
    void createNewCheckingAccount() throws Exception {
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setBalance(new BigDecimal("777777"));
        checkingAccountDTO.setPrimaryOwnerId(a1.getId());
        checkingAccountDTO.setSecondaryOwnerId(null);
        checkingAccountDTO.setCreationDate(null);
        checkingAccountDTO.setSecretKey("secretPW");

        String jsonString = objectMapper.writeValueAsString(checkingAccountDTO);
        ResultActions checkMock =mockMvc.perform(post("/banking/account/new_checking_account/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void createNewSavingsAccount() throws Exception {
        SavingsAccountDTO savingsAccountDTO = new SavingsAccountDTO();
        savingsAccountDTO.setBalance(new BigDecimal("777777"));
        savingsAccountDTO.setPrimaryOwnerId(a1.getId());
        savingsAccountDTO.setSecondaryOwnerId(null);
        savingsAccountDTO.setCreationDate(null);
        savingsAccountDTO.setSecretKey("secretPW");
        savingsAccountDTO.setInterestRate(new BigDecimal("0.4"));
        savingsAccountDTO.setMinimumBalance(new BigDecimal("1000"));

        String jsonString = objectMapper.writeValueAsString(savingsAccountDTO);
        ResultActions checkMock =mockMvc.perform(post("/banking/account/new_savings_account/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void createNewCreditCard() throws Exception {
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setBalance(new BigDecimal("777777"));
        creditCardDTO.setPrimaryOwnerId(a1.getId());
        creditCardDTO.setSecondaryOwnerId(null);
        creditCardDTO.setCreationDate(null);
        creditCardDTO.setSecretKey("secretPW");
        creditCardDTO.setInterestRate(new BigDecimal("0.1"));
        creditCardDTO.setCreditLimit(new BigDecimal("1000"));

        String jsonString = objectMapper.writeValueAsString(creditCardDTO);
        ResultActions checkMock =mockMvc.perform(post("/banking/account/new_credit_card/")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}