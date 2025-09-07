
import com.patria.apps.Apps;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Apps.class)
@Transactional
@Rollback
public abstract class BaseIntegrationTest {
}
