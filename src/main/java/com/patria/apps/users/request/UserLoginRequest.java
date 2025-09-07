package com.patria.apps.users.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(title = "Auth Login Request")
public class UserLoginRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
