package com.ubs.supermarket;

import com.ubs.supermarket.discounts.Discount;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import static java.util.Collections.emptyList;

public class Supermarket {
    private Collection<Discount> discounts = new LinkedList<>();

    public Supermarket() {
        this(emptyList());
    }

    public Supermarket(Collection<Discount> discounts) {
        this.discounts.addAll(Objects.requireNonNull(discounts));
    }

    public Receipt checkout(Basket basket) {
        return new Receipt(computeBest(Objects.requireNonNull(basket).getItems()));
    }

    private Collection<Item> computeBest(Collection<Item> items) {
        for (;;) {
            Collection<Item> currentItems = items;

            Pair pair = discounts.stream()
                .map(discount -> discount.apply(currentItems))
                .map(newItems -> new Pair(newItems, computeTotal(newItems)))
                .reduce(new Pair(currentItems, computeTotal(currentItems)), this::reducePairs);

            if (currentItems.equals(pair.items)) {
                break;
            }

            items = pair.items;
        }

        return items;
    }

    private Pair reducePairs(Pair a, Pair p) {
        if (p.total.compareTo(a.total) < 0) {
            return p;
        }
        return a;
    }

    private BigDecimal computeTotal(Collection<Item> items) {
        return items.stream()
            .parallel()
            .map(Item::getActualCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private class Pair {
        Collection<Item> items;
        BigDecimal total;

        public Pair(Collection<Item> items, BigDecimal total) {
            this.items = items;
            this.total = total;
        }
    }
}
