package com.patria.apps.response;

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
@Schema(title = "Menu Response")
public class MenuResponse {

    private String key;
    
    private String title;
    
    private String icon;
    
    private String to;
    
    private MenuResponse parent;
    
}
