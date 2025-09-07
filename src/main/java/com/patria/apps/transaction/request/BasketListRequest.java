package com.patria.apps.transaction.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Schema(title = "Basket List Request")
public class BasketListRequest {
 
    private Integer page;

    private Integer perPage;

    private String sort;
    
    private String productName;
    
    private Integer qty;
    
    private LocalDate date;
}
