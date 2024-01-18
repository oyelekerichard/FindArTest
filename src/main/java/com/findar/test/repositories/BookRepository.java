package com.findar.test.repositories;

import com.findar.test.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "Select * from book b where " +
            "(b.title like %?1% OR CAST(b.id as CHAR) like %?1% OR LOWER(b.author) like %?1%) " +
            "AND b.categories=?2",
            nativeQuery = true)
    List<Book> findAllBookByCategoriesAndKeyword(String keyword, int categories);

}