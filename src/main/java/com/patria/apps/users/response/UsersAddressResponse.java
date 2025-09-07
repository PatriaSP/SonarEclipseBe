package com.patria.apps.users.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.patria.apps.response.RoleResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "user address Response")
public class UsersAddressResponse {

    private String key;
    
    private String address;
    
}
