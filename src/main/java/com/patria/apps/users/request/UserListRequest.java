package com.patria.apps.users.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "User List Request")
public class UserListRequest {

    private Integer page;

    private Integer perPage;

    private String sort;
    
    private String name;
    
    private String country;
    
    private String email;
    
    private String role;
    
    @NotEmpty
    private Boolean isTrash;
}
