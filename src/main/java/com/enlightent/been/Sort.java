package com.enlightent.been;

import com.enlightent.util.Assert;
import com.enlightent.util.StringUtils;

import java.io.Serializable;
import java.util.*;

public class Sort implements Iterable<Sort.Order>{
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    private final List<Order> orders;

    public Sort(Order... orders) {
        this(Arrays.asList(orders));
    }

    public Sort(List<Order> orders) {
        if (null == orders || orders.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
        }
        this.orders = orders;
    }

    public Sort(String... properties) {
        this(DEFAULT_DIRECTION, properties);
    }

    public Sort(Direction direction, String... properties) {
        this(direction, properties == null ? new ArrayList<String>() : Arrays.asList(properties));
    }

    public Sort(Direction direction, List<String> properties) {
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }
        this.orders = new ArrayList<Order>(properties.size());
        for (String property : properties) {
            this.orders.add(new Order(direction, property));
        }
    }

    public Sort and(Sort sort) {
        if (sort == null) {
            return this;
        }

        ArrayList<Order> these = new ArrayList<>(this.orders);
        for (Order order : sort) {
            these.add(order);
        }
        return new Sort(these);
    }

    public Order getOrderFor(String property) {
        for (Order order : this) {
            if (order.getProperty().equals(property)) {
                return order;
            }
        }

        return null;
    }

    @Override
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Sort)) {
            return false;
        }

        Sort that = (Sort) obj;

        return this.orders.equals(that.orders);
    }

    @Override
    public int hashCode() {

        int result = 17;
        result = 31 * result + orders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return StringUtils.collectionToCammaDelimitedString(orders);
    }

    public static enum Direction{
        ASC, DESC;
        public static Direction fromString(String value) {
            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc'(case insensitive).", value), e);
            }
        }

        public static Direction fromStringOrNull(String value) {
            try {
                return fromString(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
    public static enum NullHandling{
        NATIVE, NULLS_FIRST, NULLS_LAST;
    }

    public static class Order implements Serializable {

        private static final boolean DEFAULT_IGNORE_CASE = false;

        private final Direction direction;
        private final String property;
        private final boolean ignoreCase;
        private final NullHandling nullHandling;

        public Order(Direction direction, String property) {
            this(direction, property, DEFAULT_IGNORE_CASE, null);
        }

        private Order(Direction direction, String property, NullHandling nullHandling) {
            this(direction, property, DEFAULT_IGNORE_CASE, nullHandling);
        }

        public Order(String property) {
            this(DEFAULT_DIRECTION, property);
        }

        private Order(Direction direction, String property, boolean ignoreCase, NullHandling nullHandling) {
           Assert.notBlank(property, "Property must not null or empty");

            this.direction = (direction == null ? DEFAULT_DIRECTION : direction);
            this.property = property;
            this.ignoreCase = ignoreCase;
            this.nullHandling = (nullHandling == null ? NullHandling.NATIVE : nullHandling);
        }

        public Direction getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }

        public boolean isAscending() {
            return this.direction.equals(Direction.ASC);
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public Order with(Direction order) {
            return new Order(order, this.property, nullHandling);
        }

        public Sort withProperties(String... properties) {
            return new Sort(this.direction, properties);
        }

        public Order ignoreCase() {
            return new Order(direction, property, true, nullHandling);
        }

        public Order with(NullHandling nullHandling) {
            return new Order(direction, this.property, ignoreCase, nullHandling);
        }

        public Order nullsFirst() {
            return with(NullHandling.NULLS_FIRST);
        }

        public Order nullsLast() {
            return with(NullHandling.NULLS_LAST);
        }

        public Order nullsNative() {
            return with(NullHandling.NATIVE);
        }

        public NullHandling getNullHandling() {
            return nullHandling;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            Order order = (Order) object;

            if (ignoreCase != order.ignoreCase) return false;
            if (direction != order.direction) return false;
            if (property != null ? !property.equals(order.property) : order.property != null) return false;
            return nullHandling == order.nullHandling;
        }

        @Override
        public int hashCode() {
            int result = direction != null ? direction.hashCode() : 0;
            result = 31 * result + (property != null ? property.hashCode() : 0);
            result = 31 * result + (ignoreCase ? 1 : 0);
            result = 31 * result + (nullHandling != null ? nullHandling.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            String result = String.format("%s %s", property, direction);
//            if (!NullHandling.NATIVE.equals(nullHandling)) {
//                result += ", " + nullHandling;
//            }
//            if (ignoreCase) {
//                result += ", ignoring case";
//            }

            return result;
        }
    }

}
