package com.patria.apps.controller;

import com.patria.apps.expedition.request.ExpeditionCreateRequest;
import com.patria.apps.expedition.request.ExpeditionDeleteRequest;
import com.patria.apps.expedition.request.ExpeditionDestroyRequest;
import com.patria.apps.expedition.request.ExpeditionListRequest;
import com.patria.apps.expedition.request.ExpeditionRestoreRequest;
import com.patria.apps.expedition.request.ExpeditionUpdateRequest;
import com.patria.apps.expedition.response.ExpeditionListResponse;
import com.patria.apps.expedition.service.ExpeditionService;
import com.patria.apps.response.ReadResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/expedition")
@Tag(name = "Expedition Controller")
@RequiredArgsConstructor
public class ExpeditionController {

    private final ExpeditionService expeditionService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse<List<ExpeditionListResponse>> list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "perPage", required = false, defaultValue = "5") Integer perPage,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "expeditionName", required = false) String expeditionName,
            @RequestParam(value = "price", required = false, defaultValue = "0") double price,
            @RequestParam(value = "isTrash", required = false) Boolean isTrash
    ) {
        ExpeditionListRequest request = ExpeditionListRequest.builder()
                .page(page)
                .perPage(perPage)
                .sort(sort)
                .isTrash(isTrash)
                .expeditionName(expeditionName)
                .price(price)
                .build();

        return expeditionService.list(request);
    }
    
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReadResponse<ExpeditionListResponse> retrieveSingleData(
            @PathVariable String id
    ) {
        return expeditionService.retrieveSingleData(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse create(
            HttpServletRequest servletRequest,
            @RequestBody ExpeditionCreateRequest request
            
    ) {
        return expeditionService.createExpedition(request);
    }

    @PostMapping(
            path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse update(
            HttpServletRequest servletRequest,
            @RequestBody ExpeditionUpdateRequest request
    ) {
        return expeditionService.updateExpedition(request);
    }

    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse delete(
            @RequestBody ExpeditionDeleteRequest request
    ) {
        return expeditionService.deleteExpedition(request);
    }

    @DeleteMapping(
            path = "/destroy",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse destroy(
            @RequestBody ExpeditionDestroyRequest request
    ) {
        return expeditionService.destroyExpedition(request);
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse restore(
            @RequestBody ExpeditionRestoreRequest request
    ) {
        return expeditionService.restoreExpedition(request);
    }
}
