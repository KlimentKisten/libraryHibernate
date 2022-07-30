package ru.kisten.springlearn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.kisten.springlearn.models.Book;
import ru.kisten.springlearn.models.Reader;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer>, PagingAndSortingRepository<Book, Integer>{
    List<Book> findByReader(Reader reader);
    List<Book> findAll();
    List<Book> findByNameStartingWith(String nameNotFull);
}
