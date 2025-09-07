
import com.patria.apps.expedition.request.ExpeditionCreateRequest;
import com.patria.apps.expedition.service.impl.ExpeditionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ExpeditionServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ExpeditionServiceImpl service;

    @Test
    void createExpedition_success() {
        ExpeditionCreateRequest req = new ExpeditionCreateRequest();
        req.setExpeditionName("JNE-Test");
        req.setPrice(20000.0);

        var res = service.createExpedition(req);

        assertEquals(200, res.getStatus());
    }
}
