package com.patria.apps.transaction.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.patria.apps.vo.StatusTransactionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Transactions List Request")
public class TransactionsListRequest {
 
    private Integer page;

    private Integer perPage;

    private String sort;
    
    private String invoiceNum;
        
    private StatusTransactionEnum status;
    
    private LocalDate date;
    
    //User ID
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
