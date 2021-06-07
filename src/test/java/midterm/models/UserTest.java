package midterm.models;

import midterm.models.Address;
import midterm.models.users.AccountHolder;
import midterm.models.users.Admin;
import midterm.repository.AddressRepository;
import midterm.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(profiles = "patrick")
class UserTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    AccountHolder accHolder;
    Address address;
    Admin admin;

    @BeforeAll
    public void setUp(){
        address = new Address("Default Str.9","Berlin","Germany","15xxx");
        accHolder= new AccountHolder("Peter",new Date(1,1,1),address);
        accHolder.setAddresses(address);
        addressRepository.save(address);
        userRepository.save(accHolder);


    }

    @Test
    public void test(){

    }

}