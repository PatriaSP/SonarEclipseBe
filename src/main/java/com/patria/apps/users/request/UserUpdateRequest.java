package com.patria.apps.users.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.patria.apps.vo.CountryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "User Update Request")
public class UserUpdateRequest {

    @NotEmpty
    private String key;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
    
    @NotEmpty
    private String email;

    @NotNull
    private Integer phone;
    
    @NotNull
    private boolean isActive;
    
    @Enumerated(EnumType.STRING)
    private CountryEnum country;  
   
    private List<String> address;
}
