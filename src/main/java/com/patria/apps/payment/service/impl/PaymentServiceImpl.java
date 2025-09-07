package com.patria.apps.payment.service.impl;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Payment;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.helper.MinioHelperService;
import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.helper.SortHelperService;
import com.patria.apps.helper.ValidationHelperService;
import com.patria.apps.payment.filter.PaymentFilterService;
import com.patria.apps.payment.request.PaymentCreateRequest;
import com.patria.apps.payment.request.PaymentDeleteRequest;
import com.patria.apps.payment.request.PaymentDestroyRequest;
import com.patria.apps.payment.request.PaymentListRequest;
import com.patria.apps.payment.request.PaymentRestoreRequest;
import com.patria.apps.payment.request.PaymentUpdateRequest;
import com.patria.apps.payment.response.PaymentListResponse;
import com.patria.apps.payment.serializer.PaymentSerializerService;
import com.patria.apps.payment.service.PaymentService;
import com.patria.apps.repository.CategoryRepository;
import com.patria.apps.repository.PaymentRepository;
import com.patria.apps.response.PaginationResponse;
import com.patria.apps.response.ReadResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CategoryRepository categoryRepository;
    private final SortHelperService sortHelperService;
    private final ValidationHelperService validationHelperService;
    private final AESHelperService aesService;
    private final MinioHelperService minioService;
    private final PaymentSerializerService paymentSerializerService;
    private final PaymentFilterService paymentFilterService;

    private final Map<String, String> sortMap = new HashMap<String, String>() {
        {
            //parameter and column name
            put("method", "method");
            put("-method", "method");
        }
    };

    @Override
    public ReadResponse<List<PaymentListResponse>> list(PaymentListRequest request) {
        Sort sortBy = sortHelperService.setSort(sortMap, request.getSort(), "method");
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getPerPage(), sortBy);
        Page<Payment> payment = paymentRepository.findAll(paymentFilterService.specification(request), pageable);
        List<PaymentListResponse> usersResponse = payment
                .getContent()
                .stream()
                .map(datas -> paymentSerializerService.serialize(
                datas
        ))
                .toList();

        Page<PaymentListResponse> responsePage = new PageImpl<>(usersResponse, pageable,
                payment.getTotalElements());

        return ReadResponse.<List<PaymentListResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(responsePage.getContent())
                .pagination(
                        PaginationResponse.builder()
                                .currentPage(responsePage.getNumber() + 1)
                                .totalPage(responsePage.getTotalPages())
                                .perPage(responsePage.getSize())
                                .total(responsePage.getTotalElements())
                                .count(responsePage.getNumberOfElements())
                                .hasNext(responsePage.hasNext())
                                .hasPrevious(responsePage.hasPrevious())
                                .hasContent(responsePage.hasContent())
                                .build())
                .build();
    }

    @Override
    public ReadResponse createPayment(PaymentCreateRequest request) {
        validationHelperService.validate(request);

        Payment payment = new Payment();
        payment.setMethod(request.getMethod());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setCreatedBy(0L);

        paymentRepository.save(payment);
        paymentRepository.flush();

        return paymentSerializerService.serializeTransaction(
                "Success create payment!"
        );
    }

    @Override
    public ReadResponse deletePayment(PaymentDeleteRequest request) {
        validationHelperService.validate(request);
        
        Long id = aesService.getDecryptedString(request.getKey());
        Payment payment = paymentRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        
        payment.setDeletedBy(SecurityHelperService.getPrincipal().getId());
        payment.setDeletedAt(LocalDateTime.now());
        
        paymentRepository.save(payment);
        paymentRepository.flush();
        
        return paymentSerializerService.serializeTransaction(
                "Success delete payment!"
        );
    }

    @Override
    public ReadResponse restorePayment(PaymentRestoreRequest request) {
        validationHelperService.validate(request);
        
        Long id = aesService.getDecryptedString(request.getKey());
        Payment payment = paymentRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        
        payment.setDeletedBy(null);
        payment.setDeletedAt(null);
        payment.setUpdatedBy(SecurityHelperService.getPrincipal().getId());
        payment.setUpdatedAt(LocalDateTime.now());
        
        paymentRepository.save(payment);
        paymentRepository.flush();
        
        return paymentSerializerService.serializeTransaction(
                "Success restore payment!"
        );
    }

    @Override
    public ReadResponse destroyPayment(PaymentDestroyRequest request) {
        validationHelperService.validate(request);
        
        Long id = aesService.getDecryptedString(request.getKey());
        Payment payment = paymentRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        
        paymentRepository.delete(payment);
        
        return paymentSerializerService.serializeTransaction(
                "Success destroy payment!"
        );
    }

    @Override
    public ReadResponse updatePayment(PaymentUpdateRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        payment.setMethod(request.getMethod());      
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setUpdatedBy(SecurityHelperService.getPrincipal().getId());
        
        paymentRepository.save(payment);
        paymentRepository.flush();
        

        return paymentSerializerService.serializeTransaction(
                "Success update payment!"
        );
    }

}
