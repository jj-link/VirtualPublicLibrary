package com.revature.utils;

import com.revature.models.Book;

import java.util.Comparator;

public class BookCheckedOutComparator implements Comparator<Book> {

    @Override
    public int compare(Book a, Book b) {
        return Integer.compare(a.getCheckedOutCount(), b.getCheckedOutCount());
    }

}
