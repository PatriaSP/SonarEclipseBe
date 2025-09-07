package com.patria.apps.controller;

import com.patria.apps.helper.KafkaProducerService;
import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.request.TransactionsCreateRequest;
import com.patria.apps.transaction.request.TransactionsDestroyRequest;
import com.patria.apps.transaction.request.TransactionsListRequest;
import com.patria.apps.transaction.request.TransactionsPayRequest;
import com.patria.apps.transaction.request.TransactionsProductReviewRequest;
import com.patria.apps.transaction.request.TransactionsUpdateProductReviewRequest;
import com.patria.apps.transaction.request.TransactionsUpdateRequest;
import com.patria.apps.transaction.request.TransactionsUpdateStatusRequest;
import com.patria.apps.transaction.response.TransactionsListResponse;
import com.patria.apps.transaction.serializer.TransactionsSerializerService;
import com.patria.apps.transaction.service.TransactionsService;
import com.patria.apps.vo.StatusTransactionEnum;
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
@RequestMapping("/v1/api/transaction")
@Tag(name = "Transaction Controller")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionsService transactionsService;
    private final TransactionsSerializerService transactionsSerializerService;
    private final KafkaProducerService kafkaProducerService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse<List<TransactionsListResponse>> list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "perPage", required = false, defaultValue = "5") Integer perPage,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "invoiceNum", required = false) String invoiceNum,
            @RequestParam(value = "status", required = false) StatusTransactionEnum status,
            @RequestParam(value = "date", required = false) LocalDate date
    ) {
        TransactionsListRequest request = TransactionsListRequest.builder()
                .page(page)
                .perPage(perPage)
                .sort(sort)
                .invoiceNum(invoiceNum)
                .status(status)
                .date(date)
                .build();

        return transactionsService.list(request);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse transactions(
            HttpServletRequest servletRequest,
            @RequestBody TransactionsCreateRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("transactions-create-topic", request);

        return transactionsSerializerService.serializeTransaction(
                "Success add transaction!"
        );

    }

    @DeleteMapping(
            path = "/destroy",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse transactions(
            @RequestBody TransactionsDestroyRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("transactions-destroy-topic", request);

        return transactionsSerializerService.serializeTransaction(
                "Success delete transactions!"
        );
    }

    @PostMapping(
            path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse transactions(
            HttpServletRequest servletRequest,
            @RequestBody TransactionsUpdateRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("transactions-update-topic", request);

        return transactionsSerializerService.serializeTransaction(
                "Success update transaction!"
        );

    }

    @PostMapping(
            path = "/update/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse transactions(
            HttpServletRequest servletRequest,
            @RequestBody TransactionsUpdateStatusRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("transactions-update-status-topic", request);

        return transactionsSerializerService.serializeTransaction(
                "Success update status transaction!"
        );

    }

    @PostMapping(
            path = "/pay",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse transactions(
            HttpServletRequest servletRequest,
            @RequestBody TransactionsPayRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("transactions-pay-topic", request);

        return transactionsSerializerService.serializeTransaction(
                "Payment success!"
        );

    }
    
    @PostMapping(
            path = "/product/review",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse transactions(
            HttpServletRequest servletRequest,
            @RequestBody TransactionsProductReviewRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("transactions-product-review-topic", request);

        return transactionsSerializerService.serializeTransaction(
                "Review success!"
        );

    }
    
    @PostMapping(
            path = "/product/review/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse transactions(
            HttpServletRequest servletRequest,
            @RequestBody TransactionsUpdateProductReviewRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());
        kafkaProducerService.send("transactions-update-product-review-topic", request);

        return transactionsSerializerService.serializeTransaction(
                "Review update success!"
        );

    }
}
