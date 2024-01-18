package com.findar.test.controller;

import com.findar.test.dtos.BookDto;
import com.findar.test.enums.Categories;
import com.findar.test.service.BookStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for the bookstore projects
 * Acceptance Criterias:
 * 1)Add a book
 * 2)get books by Id
 * 3)get all books
 * 4)get number of books available by Id
 * 5)update a book
 * 6)sell a book
 * 7)sell a list of books
 * 8)get book(s) by category/keywords
 * 9)get number of books sold per category/keyword
 */
@RestController
@RequestMapping("/api")
@Api(value = "Bookstore Controller", description = "Bookstore REST Endpoints.")
public class BookStoreController {

    private final BookStoreService bookStoreService;

    @Autowired
    public BookStoreController(BookStoreService bookStoreService) {
        this.bookStoreService = bookStoreService;
    }

    /**
     * AC:  1)Add a book
     * This add new book with new Identifier.
     *
     * @param bookDto
     */
    @ApiOperation(value = "Add New Book")
    @PostMapping("/add-new-book")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewBook(@Validated @RequestBody BookDto bookDto) {
        bookStoreService.addNewBook(bookDto);
    }

    /**
     * AC: 1)Add a book
     * This method add quantity of book to the books which are already registered.
     *
     * @param id
     * @param quantityToAdd
     */
    @ApiOperation(value = "Add books")
    @PutMapping("/add-book/{id}/{quantityToAdd}")
    @ResponseStatus(HttpStatus.OK)
    public void addBook(@PathVariable Long id,
                        @PathVariable int quantityToAdd) {
        bookStoreService.addBook(id, quantityToAdd);
    }

    /**
     * AC: 2)get books by id
     *
     * @param id
     * @return bookDto
     */
    @ApiOperation(value = "Get Book By Id")
    @GetMapping("/book/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookStoreService.getBookById(id);
    }


    /**
     * AC: 3)Get All Books
     *
     * @return List<BookDto>
     */
    @ApiOperation(value = "Get All Books")
    @GetMapping("/book-list")
    public List<BookDto> getAllBooks() {
        return bookStoreService.getAllBooks();
    }

    /**
     * AC: 4) Get number of books available by id.
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "Get Number of books by Id")
    @GetMapping("/number-of-books/{id}")
    public int getNumberOfBooksById(@PathVariable Long id) {
        return bookStoreService.getNumberOfBooksById(id);
    }

    /**
     * AC: 5) Update a book.
     */
    @ApiOperation(value = "Update a book")
    @PutMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateBook(@PathVariable Long id,
                           @Validated @RequestBody BookDto bookDto) {
        bookStoreService.updateBook(id, bookDto);
    }

    @ApiOperation(value = "Get Book by Category and Keyword")
    @GetMapping("/books")
    public List<BookDto> getBookByCategoryKeyWord(@RequestParam String keyword,
                                                  @RequestParam Categories categories) {
        return bookStoreService.getBookByCategoriesKeyWord(keyword, categories);
    }

}