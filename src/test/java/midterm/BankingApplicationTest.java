package midterm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "patrick")
class BankingApplicationTest {

    @Test
    void main() {
        System.out.println("Funktioniert?");
    }
}