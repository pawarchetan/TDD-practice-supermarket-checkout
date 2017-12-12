package com.ubs.supermarket;

import com.ubs.supermarket.discounts.Discount;
import com.ubs.supermarket.discounts.DiscountFixed;
import com.ubs.supermarket.discounts.DiscountNforM;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;
import org.junit.Test;

import static com.ubs.supermarket.TestFactory.aItem;
import static com.ubs.supermarket.Utils.applyScale;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class SupermarketTest {
    @Test
    public void shouldThrowNPEWhenDiscountIsNull() {
        assertThatThrownBy(() -> new Supermarket(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowNPEWhenBasketIsNull() {
        assertThatThrownBy(() -> new Supermarket().checkout(null)).isInstanceOf(NullPointerException.class);
    }
    @Test
    public void shouldProduceAReceiptWithTotalEqualsZeroForAnEmptyBasket() {
        // given
        final Supermarket supermarket = new Supermarket();

        final Basket basket = Basket.builder().build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(receipt.getTotal()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsOneItem() {
        // given
        final Supermarket supermarket = new Supermarket();

        final Item item = aItem("apples", 1.0, 1, Unit.KG);

        final Basket basket = Basket.builder()
            .addItem(item)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(1.0), 4));
    }

    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsTwoItems() {
        // given
        final Supermarket supermarket = new Supermarket();

        final Item item1 = aItem("apples", 1.0, 1, Unit.KG);
        final Item item2 = aItem("oranges", 0.9, 2, Unit.KG);

        final Basket basket = Basket.builder()
            .addItem(item1)
            .addItem(item2)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(2.8), 4));
    }

    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsSameTwoItemsAndCanApplyDiscount2X1() {
        // given
        final Collection<Discount> discounts = Collections.singletonList(new DiscountNforM("Eggs", 2, 1));

        final Supermarket supermarket = new Supermarket(discounts);

        final Item item1 = aItem("Eggs", 2.0);
        final Item item2 = aItem("Eggs", 2.0);

        final Basket basket = Basket.builder()
            .addItem(item1)
            .addItem(item2)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(2.0), 4));
    }
    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsSameThreeItemsAndCanApplyDiscount2X1() {
        // given
        final Collection<Discount> discounts = Collections.singletonList(new DiscountNforM("Eggs", 2, 1));

        final Supermarket supermarket = new Supermarket(discounts);

        final Item item1 = aItem("Eggs", 2.0);
        final Item item2 = aItem("Eggs", 2.0);
        final Item item3 = aItem("Eggs", 2.0);

        final Basket basket = Basket.builder()
            .addItem(item1)
            .addItem(item2)
            .addItem(item3)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(4.0), 4));
    }

    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsSameFourItemsAndCanApplyDiscount2X1() {
        // given
        final Collection<Discount> discounts = Collections.singletonList(new DiscountNforM("Eggs", 2, 1));

        final Supermarket supermarket = new Supermarket(discounts);

        final Item item1 = aItem("Eggs", 2.0);
        final Item item2 = aItem("Eggs", 2.0);
        final Item item3 = aItem("Eggs", 2.0);
        final Item item4 = aItem("Eggs", 2.0);

        final Basket basket = Basket.builder().addItem(item1)
            .addItem(item2)
            .addItem(item3)
            .addItem(item4)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(4.0), 4));
    }

    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsSameFourItemsAndCanApplyDiscount3X2() {
        // given
        final Collection<Discount> discounts = Collections.singletonList(new DiscountNforM("Eggs", 3, 2));

        final Supermarket supermarket = new Supermarket(discounts);

        final Item item1 = aItem("Eggs", 2.0);
        final Item item2 = aItem("Eggs", 2.0);
        final Item item3 = aItem("Eggs", 2.0);
        final Item item4 = aItem("Eggs", 2.0);

        final Basket basket = Basket.builder()
            .addItem(item1)
            .addItem(item2)
            .addItem(item3)
            .addItem(item4)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(6.0), 4));
    }
    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsFourItemsAndCanApplyTwoDiscounts()
    {
        // given
        final Collection<Discount> discounts = asList(new DiscountNforM("Eggs", 3, 2), new DiscountFixed("sensonite", new BigDecimal(1.7)));

        final Supermarket supermarket = new Supermarket(discounts);

        final Item item1 = aItem("Eggs", 2.0);
        final Item item2 = aItem("Eggs", 2.0);
        final Item item3 = aItem("Eggs", 2.0);
        final Item item4 = aItem("sensonite", 2.0);

        final Basket basket = Basket.builder()
            .addItem(item1)
            .addItem(item2)
            .addItem(item3)
            .addItem(item4)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(5.7), 4));
    }

    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsFourItemsAndCanApplyTwoDiscountsOnSameItems() {
        // given
        final Collection<Discount> discounts = asList(new DiscountNforM("Eggs", 3, 2), new DiscountFixed("Eggs", new BigDecimal(1.7)));

        final Supermarket supermarket = new Supermarket(discounts);

        final Item item1 = aItem("Eggs", 2.0);
        final Item item2 = aItem("Eggs", 2.0);
        final Item item3 = aItem("Eggs", 2.0);
        final Item item4 = aItem("Eggs", 2.0);

        final Basket basket = Basket.builder()
            .addItem(item1)
            .addItem(item2)
            .addItem(item3)
            .addItem(item4)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(5.7), 4));
    }
    @Test
    public void shouldApplyBestDiscountWhenBasketContainsThreeItemsAndCanApplyTwoDiscountsOnSameItems() {
        // given
        final Collection<Discount> discounts = asList(new DiscountNforM("Eggs", 3, 2), new DiscountFixed("Eggs", new BigDecimal(1)));

        final Supermarket supermarket = new Supermarket(discounts);

        final Item item1 = aItem("Eggs", 2.0);
        final Item item2 = aItem("Eggs", 2.0);
        final Item item3 = aItem("Eggs", 2.0);

        final Basket basket = Basket.builder()
            .addItem(item1)
            .addItem(item2)
            .addItem(item3)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(3.0), 4));
    }

    @Test
    public void shouldApplyBestDiscountWhenBasketContainsFiveItemsAndCanApplyTwoDiscountsOnSameItems() {
        // given
        final Collection<Discount> discounts = asList(new DiscountNforM("Eggs", 3, 1), new DiscountNforM("Eggs", 2, 1));

        final Supermarket supermarket = new Supermarket(discounts);

        final Item item1 = aItem("Eggs", 2.0);
        final Item item2 = aItem("Eggs", 2.0);
        final Item item3 = aItem("Eggs", 2.0);
        final Item item4 = aItem("Eggs", 2.0);
        final Item item5 = aItem("Eggs", 2.0);

        final Basket basket = Basket.builder()
            .addItem(item1)
            .addItem(item2)
            .addItem(item3)
            .addItem(item4)
            .addItem(item5)
            .build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(4.0), 4));
    }

    @Test
    public void shouldProduceAReceiptWithCorrectTotalWhenBasketContainsManyItems() {
        // given
        final Collection<Discount> discounts = asList(new DiscountNforM("Eggs", 2, 1), new DiscountFixed("Eggs", new BigDecimal(0.9)));

        final Supermarket supermarket = new Supermarket(discounts);

        final Basket.Builder builder = Basket.builder();

        IntStream.range(0, 200).forEach(i -> builder.addItem(aItem("Eggs", 2.0)));

        final Basket basket = builder.build();

        // when
        final Receipt receipt = supermarket.checkout(basket);

        // then
        assertThat(applyScale(receipt.getTotal(), 4)).isEqualTo(applyScale(new BigDecimal(180.0), 4));
    }
}
