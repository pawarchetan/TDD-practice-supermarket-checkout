package com.ubs.supermarket;

import org.junit.Test;

import static com.ubs.supermarket.TestFactory.aItem;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class BasketTest {
    @Test
    public void shouldNotThrowNPEWhenItemsAreMissing() {
        assertThat(Basket.builder().build()).isNotNull();
    }

    @Test
    public void shouldThrowNPEWhenItemsIsNull() {
        assertThatThrownBy(() -> Basket.builder().addItem(null).build()).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldReturnOneItem() {
        final Item actualItem = aItem("item", (double) 1, 1, Unit.KG);
        final Item expectedItem = aItem("item", (double) 1, 1, Unit.KG);

        assertThat(Basket.builder().addItem(actualItem).build().getItems()).containsExactly(expectedItem);
    }

    @Test
    public void shouldReturnTwoItems() {
        final Item actualItem1 = aItem("item1", (double) 2, 1, Unit.KG);
        final Item actualItem2 = aItem("item2", (double) 3, 1, Unit.KG);

        final Item expectedItem1 = aItem("item1", (double) 2, 1, Unit.KG);
        final Item expectedItem2 = aItem("item2", (double) 3, 1, Unit.KG);

        final Basket basket = Basket.builder()
            .addItem(actualItem1)
            .addItem(actualItem2)
            .build();

        assertThat(basket.getItems()).containsExactlyInAnyOrder(expectedItem1, expectedItem2);
    }

}
