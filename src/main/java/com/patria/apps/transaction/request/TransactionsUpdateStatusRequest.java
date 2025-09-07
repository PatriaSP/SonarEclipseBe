package com.patria.apps.transaction.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.patria.apps.vo.StatusTransactionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Transactions Update Status Request")
public class TransactionsUpdateStatusRequest {

    @NotEmpty
    private String key;
    
    @NotNull
    private StatusTransactionEnum status;
    
    //User ID
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
