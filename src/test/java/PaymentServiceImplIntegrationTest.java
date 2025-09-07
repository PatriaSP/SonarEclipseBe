
import com.patria.apps.payment.request.PaymentCreateRequest;
import com.patria.apps.payment.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private PaymentServiceImpl service;

    @Test
    void createPayment_success() {
        PaymentCreateRequest req = new PaymentCreateRequest();
        req.setMethod("CREDIT_CARD");

        var res = service.createPayment(req);

        assertEquals(200, res.getStatus());
    }
}
