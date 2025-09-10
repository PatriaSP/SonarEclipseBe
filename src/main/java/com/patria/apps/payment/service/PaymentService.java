package com.patria.apps.payment.service;

import com.patria.apps.payment.request.PaymentCreateRequest;
import com.patria.apps.payment.request.PaymentDeleteRequest;
import com.patria.apps.payment.request.PaymentDestroyRequest;
import com.patria.apps.payment.request.PaymentListRequest;
import com.patria.apps.payment.request.PaymentRestoreRequest;
import com.patria.apps.payment.request.PaymentUpdateRequest;
import com.patria.apps.payment.response.PaymentListResponse;
import com.patria.apps.response.ReadResponse;
import java.util.List;

public interface PaymentService {

    ReadResponse<List<PaymentListResponse>> list(PaymentListRequest request);

    ReadResponse createPayment(PaymentCreateRequest request);

    ReadResponse deletePayment(PaymentDeleteRequest request);

    ReadResponse restorePayment(PaymentRestoreRequest request);

    ReadResponse destroyPayment(PaymentDestroyRequest request);

    ReadResponse updatePayment(PaymentUpdateRequest request);

    ReadResponse<PaymentListResponse> retrieveSingleData(String id);

}
