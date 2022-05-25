## User Model

- int userId
- String email
- String password
- String firstName
- String lastName
- list<Book> checkedOut
- enum userRole


## Book

- int bookId
- String title 
- String author
- int genreID
- String summary
- int checkedOutCount
- long isbn
- int yearPublished


## Genre Lookup Table

- 1. Fantasy
- 2. History
- 3. Romance
- 4. Sci-fi
- 5. Cook book
- 6. Biography
- 7. Thriller
- 8. Period Drama
- 9. Comedy
- 10. Children's
- 11. Mystery
- 12. Mangas
- 13. Graphic Novels
- 14. Technology