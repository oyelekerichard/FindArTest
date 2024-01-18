package com.findar.test.service;

import com.findar.test.dtos.BookDto;
import com.findar.test.entities.Book;
import com.findar.test.enums.Categories;
import com.findar.test.exceptions.BadRequestException;
import com.findar.test.exceptions.BookNotFoundException;
import com.findar.test.exceptions.DuplicateResourceException;
import com.findar.test.repositories.BookRepository;
import com.findar.test.serviceimpl.BookStoreServiceImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {
    private final Long id = 1234L;
    private final Categories category = Categories.ACTION;
    private final int totalCount = 2;
    private final String keyword = "keyword";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private BookRepository bookRepository;


    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookStoreServiceImpl sut;

    @Test
    public void testAddNewBook() {
        //Arrange
        BookDto bookDto = mock(BookDto.class);
        Book book = mock(Book.class);
        when(bookDto.getId()).thenReturn(id);
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);

        //Act
        sut.addNewBook(bookDto);

        //Verify
        verify(bookRepository).save(book);
    }

    @Test
    public void testAddNewBook_Given_IdIsPresent_Then_ThrowsDuplicateResourceException() {
        thrown.expect(DuplicateResourceException.class);
        thrown.expectMessage("Book with same id present. " +
                "Either use update methods to update the book counts or use addBook(Long id, int quantityToAdd) methods");

        //Arrange
        BookDto bookDto = mock(BookDto.class);
        Book book = mock(Book.class);
        when(bookDto.getId()).thenReturn(id);
        when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));

        //Act
        sut.addNewBook(bookDto);
    }

    @Test
    public void testAddBook() {
        //Arrange
        Book book = mock(Book.class);
        when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));
        when(book.getTotalCount()).thenReturn(totalCount);

        //Act
        sut.addBook(id, 1);

        //Verify
        verify(bookRepository).save(book);
    }

    @Test
    public void testAddBook_Given_NoBookIsFoundById_Then_ThrowsBookNotFoundException() {
        thrown.expect(BookNotFoundException.class);
        thrown.expectMessage("Book with id:" + id + " is not registered.");
        //Arrange
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        sut.addBook(id, 1);

    }

    @Test
    public void testGetBookById() {
        //Arrange
        Book book = mock(Book.class);
        BookDto bookDto = mock(BookDto.class);
        when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        //Act
        BookDto actualBookDto = sut.getBookById(id);

        //Assert
        assertEquals(bookDto, actualBookDto);
    }

    @Test
    public void testGetBookById_Given_NoBookIsFoundForId_Then_ThrowsBookNotFoundException() {
        thrown.expect(BookNotFoundException.class);
        thrown.expectMessage("Book with id:" + id + " is not found.");
        //Arrange
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        sut.getBookById(id);
    }

    @Test
    public void testGetAllBooks() {
        //Arrange
        Book book = mock(Book.class);
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);

        BookDto bookDto = mock(BookDto.class);
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(bookDto);
        when(bookRepository.findAll()).thenReturn(bookList);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        //Act
        List<BookDto> actualBookDto = sut.getAllBooks();

        //Assert
        assertEquals(bookDtoList, actualBookDto);
    }

    @Test
    public void testGetNumberOfBooksById() {
        //Arrange
        Book book = mock(Book.class);
        when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));
        when(book.getTotalCount()).thenReturn(totalCount);

        //Act
        int actualNumberOfBooks = sut.getNumberOfBooksById(id);

        //Assert
        assertEquals(totalCount, actualNumberOfBooks);
    }

    @Test
    public void testGetNumberOfBooksById_Given_NoBookIsPresent() {
        //Arrange
        Book book = mock(Book.class);
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        int actualNumberOfBooks = sut.getNumberOfBooksById(id);

        //Assert
        assertEquals(0, actualNumberOfBooks);
    }

    @Test
    public void testUpdateBook() {
        //Arrange
        BookDto bookDto = mock(BookDto.class);
        Book book = mock(Book.class);
        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);
        when(bookDto.getId()).thenReturn(id);
        Book bookFromRepo = mock(Book.class);
        when(bookRepository.getOne(id)).thenReturn(bookFromRepo);
        //Act
        sut.updateBook(id, bookDto);

        //Assert
        verify(bookRepository).save(book);
    }

    @Test
    public void testUpdateBook_Given_IdIsChange_Then_ThrowsBadRequestException() {
        thrown.expect(BadRequestException.class);
        thrown.expectMessage("Id cannot be updated.");
        //Arrange
        BookDto bookDto = mock(BookDto.class);
        Book book = mock(Book.class);
        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);
        when(bookDto.getId()).thenReturn(43L);
        //Act
        sut.updateBook(id, bookDto);
    }

    @Test
    public void testGetBookByCategoryKeyword() {
        //Arrange
        Book book = mock(Book.class);
        List<Book> books = new ArrayList<>();
        books.add(book);

        BookDto bookDto = mock(BookDto.class);
        List<BookDto> bookDtos = new ArrayList<>();
        bookDtos.add(bookDto);
        when(bookRepository.findAllBookByCategoriesAndKeyword(keyword.toLowerCase(), category.getValue())).thenReturn(books);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        //Act
        List<BookDto> actualBookDtos = sut.getBookByCategoriesKeyWord(keyword, category);

        //Assert
        assertEquals(bookDtos, actualBookDtos);
    }
}