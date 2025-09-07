package com.patria.apps.controller;

import com.patria.apps.helper.KafkaProducerService;
import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.request.BasketCreateRequest;
import com.patria.apps.transaction.request.BasketUpdateRequest;
import com.patria.apps.transaction.request.BasketListRequest;
import com.patria.apps.transaction.request.BasketDestroyRequest;
import com.patria.apps.transaction.response.BasketListResponse;
import com.patria.apps.transaction.serializer.BasketSerializerService;
import com.patria.apps.transaction.service.BasketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/basket")
@Tag(name = "Basket Controller")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;
    private final BasketSerializerService basketSerializerService;
    private final KafkaProducerService kafkaProducerService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse<List<BasketListResponse>> basket(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "perPage", required = false, defaultValue = "5") Integer perPage,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "date", required = false) LocalDate date
    ) {
        BasketListRequest request = BasketListRequest.builder()
                .page(page)
                .perPage(perPage)
                .sort(sort)
                .productName(productName)
                .date(date)
                .build();

        return basketService.list(request);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse basket(
            HttpServletRequest servletRequest,
            @RequestBody BasketCreateRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("basket-create-topic", request);

        return basketSerializerService.serializeTransaction(
                "Success add basket!"
        );

    }

    @PostMapping(
            path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse basket(
            HttpServletRequest servletRequest,
            @RequestBody BasketUpdateRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("basket-update-topic", request);

        return basketSerializerService.serializeTransaction(
                "Success edit basket!"
        );

    }

    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse basket(
            @RequestBody BasketDestroyRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("basket-destroy-topic", request);

        return basketSerializerService.serializeTransaction(
                "Success delete basket!"
        );
    }
    
    
}
