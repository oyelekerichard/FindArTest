package com.findar.test.service;

import com.findar.test.dtos.BookDto;
import com.findar.test.enums.Categories;

import java.util.List;

public interface BookStoreService {
    void addNewBook(BookDto bookDto);

    void addBook(Long id, int quantityToAdd);

    BookDto getBookById(Long id);

    List<BookDto> getAllBooks();

    int getNumberOfBooksById(Long id);

    void updateBook(Long id, BookDto bookDto);

    List<BookDto> getBookByCategoriesKeyWord(String keyword, Categories categories);

}