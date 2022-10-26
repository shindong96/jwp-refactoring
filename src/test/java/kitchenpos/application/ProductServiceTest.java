package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.support.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
    }

    @DisplayName("상품을 저장한다.")
    @Test
    void create() {
        // given
        final Product product = new Product("후라이드", new BigDecimal(17000));

        // when
        final Product savedProduct = productService.create(product);

        // then
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo("후라이드")
        );
    }

    @DisplayName("상품의 가격이 null이면 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceIsNull() {
        // given
        final Product product = new Product("후라이드", null);

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 올바르지 않습니다.");
    }

    @DisplayName("상품의 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceNotPositive() {
        // given
        final Product product = new Product("후라이드", new BigDecimal(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 올바르지 않습니다.");
    }

    @DisplayName("전체 상품을 조회한다.")
    @Test
    void findAll() {
        // given
        productDao.save(new Product("후라이드", new BigDecimal(10000)));

        // when, then
        assertThat(productService.findAll()).hasSize(1);
    }
}
