package com.patria.apps.expedition.request;

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
@Schema(title = "Expedition List Request")
public class ExpeditionListRequest {

    private Integer page;

    private Integer perPage;

    private String sort;
    
    private String expeditionName;
    
    private double price;

    private Boolean isTrash;
}
