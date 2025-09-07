package com.patria.apps.expedition.request;

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
@Schema(title = "Expedition Update Request")
public class ExpeditionUpdateRequest {

    @NotEmpty
    private String key;
    
    @NotEmpty
    private String expeditionName;
    
    @NotNull
    private double price;

}
