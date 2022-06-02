package com.revature.utils;

import com.revature.models.Book;

import java.util.Comparator;

public class BookIdComparator implements Comparator<Book> {

    @Override
    public int compare(Book a, Book b) {
        return Integer.compare(a.getBookId(), b.getBookId());
    }

}
