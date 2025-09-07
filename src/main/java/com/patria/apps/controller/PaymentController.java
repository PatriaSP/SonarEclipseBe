package com.patria.apps.controller;

import com.patria.apps.payment.request.PaymentCreateRequest;
import com.patria.apps.payment.request.PaymentDeleteRequest;
import com.patria.apps.payment.request.PaymentDestroyRequest;
import com.patria.apps.payment.request.PaymentListRequest;
import com.patria.apps.payment.request.PaymentRestoreRequest;
import com.patria.apps.payment.request.PaymentUpdateRequest;
import com.patria.apps.payment.response.PaymentListResponse;
import com.patria.apps.payment.service.PaymentService;
import com.patria.apps.response.ReadResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/payment")
@Tag(name = "Payment Controller")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse<List<PaymentListResponse>> list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "perPage", required = false, defaultValue = "5") Integer perPage,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "method", required = false) String method
    ) {
        PaymentListRequest request = PaymentListRequest.builder()
                .page(page)
                .perPage(perPage)
                .sort(sort)
                .method(method)
                .build();

        return paymentService.list(request);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse create(
            HttpServletRequest servletRequest,
            @RequestBody PaymentCreateRequest request
            
    ) {
        return paymentService.createPayment(request);
    }

    @PostMapping(
            path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse update(
            HttpServletRequest servletRequest,
            @RequestBody PaymentUpdateRequest request
    ) {
        return paymentService.updatePayment(request);
    }

    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse delete(
            @RequestBody PaymentDeleteRequest request
    ) {
        return paymentService.deletePayment(request);
    }

    @DeleteMapping(
            path = "/destroy",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse destroy(
            @RequestBody PaymentDestroyRequest request
    ) {
        return paymentService.destroyPayment(request);
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse restore(
            @RequestBody PaymentRestoreRequest request
    ) {
        return paymentService.restorePayment(request);
    }
}
