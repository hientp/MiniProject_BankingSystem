package midterm.models.users;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class ThirdParty extends User{

    UUID hashedKey = new UUID(10,2);

    public ThirdParty() {
        super();
    }

    public ThirdParty(String name, UUID hashedKey) {
        super(name);
        this.hashedKey = hashedKey;
    }

    public UUID getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(UUID hashedKey) {
        this.hashedKey = hashedKey;
    }
}
