package com.findar.test.serviceimpl;

import com.findar.test.dtos.BookDto;
import com.findar.test.entities.Book;
import com.findar.test.enums.Categories;
import com.findar.test.exceptions.BadRequestException;
import com.findar.test.exceptions.BookNotFoundException;
import com.findar.test.exceptions.DuplicateResourceException;
import com.findar.test.repositories.BookRepository;
import com.findar.test.service.BookStoreService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookStoreServiceImpl implements BookStoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookStoreServiceImpl.class);
    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public BookStoreServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Register new book with new identifier into database
     * Save the book In Book Domain
     * Maintain the count in BookCount Domain
     *
     * @param bookDto
     */
    @Override
    @Transactional
    public void addNewBook(BookDto bookDto) {
        //Check if bookDto is previously present
        Optional<Book> bookById = bookRepository.findById(bookDto.getId());
        bookById.ifPresent(book -> {
            throw new DuplicateResourceException("Book with same id present. " +
                    "Either use update methods to update the book counts or use addBook(Long id, int quantityToAdd) methods");
        });
        if (!bookById.isPresent()) {
            LOGGER.info("No Duplicates found.");
            //Map bookDto to book
            Book book = modelMapper.map(bookDto, Book.class);
            //Set the status to available
            LOGGER.info("The data are mapped and ready to save.");

            //Save to book
            bookRepository.save(book);
        }
    }

    /**
     * This method adds the quantity of book if the book with given id is already registered.
     *
     * @param id
     * @param quantityToAdd
     */
    @Override
    public void addBook(Long id, int quantityToAdd) {
        //Get the book by id
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id:" + id + " is not registered. Use addNewBook to register."));
        LOGGER.info("The book with id " + id + " is registered");

        int totalCountAfterAdd = book.getTotalCount() + quantityToAdd;
        book.setTotalCount(totalCountAfterAdd);

        bookRepository.save(book);
    }

    /**
     * Get book by id
     *
     * @param id
     * @return bookdto
     */
    @Override
    public BookDto getBookById(Long id) {
        //Get the book from repo
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id:" + id + " is not found."));

        return modelMapper.map(book, BookDto.class);
    }


    /**
     * List all the books
     *
     * @return List<BookDto>
     */
    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return mapBookListToBooDtoList(books);
    }

    /**
     * Number of books on particular identifier
     *
     * @param id
     * @return
     */
    @Override
    public int getNumberOfBooksById(Long id) {
        Optional<Book> book = bookRepository.findById(id);

        //If book is present get Total Count else return 0
        return book.isPresent() ? book.get().getTotalCount() : 0;
    }

    /**
     * update a book
     *
     * @param id
     * @param bookDto
     */
    @Override
    @Transactional
    public void updateBook(Long id, BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        if (bookDto.getId() != null) {
            if (!bookDto.getId().equals(id)) {
                throw new BadRequestException("Id cannot be updated.");
            }
        }
        //If id is removed from bookDto, it still sets the id from pathvariable
        book.setId(id);
        LOGGER.info("BookDto are mapped to Book and ready to be saved.");
        bookRepository.save(book);
    }


    /**
     * Get the list of books according to category and keyword
     * Keyword is assumed to be any words in id, title and author field of the book
     *
     * @param categories
     * @param keyword
     * @return
     */
    @Override
    public List<BookDto> getBookByCategoriesKeyWord(String keyword,
                                                    Categories categories) {

        //if the status is Available, gives list of books which are available
        LOGGER.info("Fetch all the books by category and keyword.");
        List<Book> book = bookRepository.findAllBookByCategoriesAndKeyword(keyword.toLowerCase(), categories.getValue());
        return mapBookListToBooDtoList(book);
    }

    //Convert List of books to List of bookDto
    private List<BookDto> mapBookListToBooDtoList(List<Book> books) {
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

}