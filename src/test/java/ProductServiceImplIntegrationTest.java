
import com.patria.apps.exception.GeneralException;
import com.patria.apps.product.request.ProductCreateRequest;
import com.patria.apps.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProductServiceImpl service;

    @Test
    void createProduct_success() {
        ProductCreateRequest req = new ProductCreateRequest();
        req.setName("Laptop-Test");
        req.setCategoryKey("8CWpNspq7fouXqQc5UoIJg=="); 
        req.setPrice(1000);
        req.setStock(10);
        req.setId(1L);

        var res = service.createProduct(req);

        assertEquals(200, res.getStatus());
    }

    @Test
    void createProduct_categoryNotFound() {
        ProductCreateRequest req = new ProductCreateRequest();
        req.setCategoryKey("InvalidKey");

        assertThrows(GeneralException.class, () -> service.createProduct(req));
    }
}
