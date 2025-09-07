package com.patria.apps.users.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.patria.apps.response.RoleResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
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
@Schema(title = "user Response")
public class UsersResponse {

    private String key;
    
    private String fullName;
    
    private String email;
    
    private String country;
    
    private String phone;
    
    private boolean isActive;
    
    private LocalDateTime lastLoginAt;
    
    private RoleResponse role;
    
    private List<UsersAddressResponse> userAddress;
    
   
}
