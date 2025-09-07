package com.patria.apps.transaction.request;

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
@Schema(title = "Transactions Update Request")
public class TransactionsUpdateRequest {

    @NotEmpty
    private String key;
    
    @NotEmpty
    private String paymentKey;
    
    @NotEmpty
    private String userAddressKey;

    //User ID
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
