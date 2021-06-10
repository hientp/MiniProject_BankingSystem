package midterm.controller.dto;

import midterm.models.Address;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class FirstPartyUserDTO {

    @NotNull @NotEmpty
    private String name;
    private Date birthDay;
    private Integer addressId;

    public FirstPartyUserDTO() {
    }

    public FirstPartyUserDTO(String name, Date birthDay, Integer addressId) {
        this.name = name;
        this.birthDay = birthDay;
        this.addressId = addressId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}
