package midterm.models.users;

import midterm.models.Address;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Admin extends FirstPartyUser{

    public Admin() {
        super();
    }

    public Admin(String name, Date birthDay, Address address) {
        super(name, birthDay, address);
    }

}
