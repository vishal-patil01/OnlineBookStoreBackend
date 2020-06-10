package com.enigma.bookstore.enums;

import org.springframework.data.domain.Sort;

public enum FilterAttributes {
    DEFAULT(Sort.by("book_name")),
    QUANTITY_LOW_TO_HIGH(Sort.by("no_of_copies")),
    LOW_TO_HIGH(Sort.by("book_price")),
    HIGH_TO_LOW(Sort.by("book_price").descending()),
    NEW_ARRIVALS(Sort.by("publishing_year").descending());
    public Sort sort;

    FilterAttributes(Sort sort) {
        this.sort = sort;
    }
}