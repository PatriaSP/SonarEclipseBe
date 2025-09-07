package com.patria.apps.transaction.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.patria.apps.expedition.response.ExpeditionHistoryResponse;
import com.patria.apps.product.response.ProductReviewResponse;
import com.patria.apps.vo.StatusTransactionEnum;
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
@Schema(title = "Transactions List Response")
public class TransactionsListResponse {

    private String key;
    
    private String invoiceNum;
    
    private StatusTransactionEnum status;
    
    private int qty;
    
    private LocalDateTime date;
    
    private String productName;
    
    private String payment;
    
    private String userName;
    
    private String address;
    
    private String expedition;
    
    private List<ExpeditionHistoryResponse> expeditionHistory;
    
    private ProductReviewResponse productReviewResponse;
}
