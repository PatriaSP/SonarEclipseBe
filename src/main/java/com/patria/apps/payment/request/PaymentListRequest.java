package com.patria.apps.payment.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Payment List Request")
public class PaymentListRequest {

    private Integer page;

    private Integer perPage;

    private String sort;
        
    private String method;

    private Boolean isTrash;
}
