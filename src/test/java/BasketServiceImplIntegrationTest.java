
import com.patria.apps.entity.Category;
import com.patria.apps.entity.Product;
import com.patria.apps.entity.Users;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.repository.CategoryRepository;
import com.patria.apps.repository.ProductRepository;
import com.patria.apps.repository.UsersRepository;
import com.patria.apps.transaction.request.BasketCreateRequest;
import com.patria.apps.transaction.service.impl.BasketServiceImpl;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpStatus;

class BasketServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private BasketServiceImpl service;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private com.patria.apps.config.AESHelperService aesService;

    @Test
    void createBasket_success() {
        BasketCreateRequest req = new BasketCreateRequest();
        req.setId(1L);
        req.setQty(1);
        req.setProductKey("8CWpNspq7fouXqQc5UoIJg==");

        var res = service.createBasket(req);

        assertEquals(200, res.getStatus());
    }

    @Test
    void createBasket_outOfStock() {
        Users user = new Users();
        user.setUsername("test-user");
        user.setPassword("secret");
        user.setEmail("test@example.com");
        user.setCreatedBy(0L);
        user.setCreatedAt(LocalDateTime.now());
        usersRepository.save(user);

        Category category = categoryRepository.findById(1L).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Category Not Found")
        );

        Product product = new Product();
        product.setName("Laptop-Test");
        product.setStock(0);
        product.setPrice(1000.0);
        product.setImage("Shirt/Laptop-Test");
        product.setActive(true);
        product.setCategory(category);
        product.setCreatedBy(0L);
        product.setCreatedAt(LocalDateTime.now());
        productRepository.saveAndFlush(product);

        String productKey = null;
        try {
            productKey = aesService.encrypt(String.valueOf(product.getId()));
        } catch (Exception ex) {
            System.getLogger(BasketServiceImplIntegrationTest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        BasketCreateRequest req = new BasketCreateRequest();
        req.setId(user.getId());
        req.setQty(1);
        req.setProductKey(productKey);

        assertThrows(GeneralException.class, () -> service.createBasket(req));
    }
}
