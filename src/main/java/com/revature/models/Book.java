package com.revature.models;

public class Book {

    private int bookId;
    private String title;
    private String author;
    private int genreId;
    private String summary;
    private int checkedOutCount;
    private long isbn;
    private int yearPublished;


    public Book() {
    }

    public Book(int bookId, String title, String author, int genreId, String summary, int checkedOutCount, long isbn, int yearPublished) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genreId = genreId;
        this.summary = summary;
        this.checkedOutCount = checkedOutCount;
        this.isbn = isbn;
        this.yearPublished = yearPublished;
    }

    public Book(String title, String author, int genreId, String summary, long isbn, int yearPublished) {
        this.title = title;
        this.author = author;
        this.genreId = genreId;
        this.summary = summary;
        this.checkedOutCount = 0;
        this.isbn = isbn;
        this.yearPublished = yearPublished;

    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getCheckedOutCount() {
        return checkedOutCount;
    }

    public void setCheckedOutCount(int checkedOutCount) {
        this.checkedOutCount = checkedOutCount;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genreId=" + genreId +
                ", summary='" + summary + '\'' +
                ", checkedOutCount=" + checkedOutCount +
                ", isbn=" + isbn +
                ", yearPublished=" + yearPublished +
                '}';
    }
}
