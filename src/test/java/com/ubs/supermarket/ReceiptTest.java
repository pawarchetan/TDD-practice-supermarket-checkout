package com.ubs.supermarket;

import com.ubs.supermarket.discounts.Discount;
import com.ubs.supermarket.discounts.DiscountFixed;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReceiptTest {
    @Test
    public void shouldThrowNPEWhenItemsIsNull() {
        assertThatThrownBy(() -> new Receipt(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldReturnTotal() {
        final Receipt receipt = new Receipt(Collections.singletonList(TestFactory.aItem("beans", 1)));
        assertThat(receipt.getTotal()).isEqualTo(new BigDecimal(1));
    }

    @Test
    public void shouldReturnSubTotal() {
        final Discount discount = new DiscountFixed("beans", new BigDecimal(0.7));
        final Receipt receipt = new Receipt(Collections.singletonList(TestFactory.aItem("beans", 1, discount, 0.7)));
        assertThat(Utils.applyScale(receipt.getSubTotal(), 4)).isEqualTo(Utils.applyScale(new BigDecimal(1.0), 4));
    }

    @Test
    public void shouldIncludeDiscountInTotal() {
        final Discount discount = new DiscountFixed("beans", new BigDecimal(0.7));
        final Receipt receipt = new Receipt(Collections.singletonList(TestFactory.aItem("beans", 1, discount, 0.7)));
        assertThat(Utils.applyScale(receipt.getTotal(), 4)).isEqualTo(Utils.applyScale(new BigDecimal(0.7), 4));
    }
}
