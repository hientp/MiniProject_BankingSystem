package midterm.controller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

public class ThirdPartyDTO {

    @NotNull @NotEmpty
    private String name;
    @NotNull
    private UUID hashedKey;

    public ThirdPartyDTO() {
    }

    public ThirdPartyDTO(String name, UUID hashedKey) {
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(UUID hashedKey) {
        this.hashedKey = hashedKey;
    }
}
