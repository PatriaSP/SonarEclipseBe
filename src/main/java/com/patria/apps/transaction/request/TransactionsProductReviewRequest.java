package com.patria.apps.transaction.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Schema(title = "Transactions Product Review Request")
public class TransactionsProductReviewRequest {
    
    @NotEmpty
    private String productKey;
    
    @NotEmpty
    private String transactionKey;
    
    @NotEmpty
    private String review;
    
    @NotNull
    private int score;
    
    //User ID
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
