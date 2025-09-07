package com.patria.apps.transaction.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Schema(title = "Basket List Response")
public class BasketListResponse {

    private String key;
    
    private String productKey;
    
    private String productName;
    
    private Integer qty;
    
    private LocalDateTime date;
}
