package midterm.models.users;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class ThirdParty extends User{

    @NotNull
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
