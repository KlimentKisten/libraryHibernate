package ru.kisten.springlearn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kisten.springlearn.models.Book;
import ru.kisten.springlearn.models.Reader;
import ru.kisten.springlearn.repositories.BooksRepository;
import ru.kisten.springlearn.repositories.ReadersRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final ReadersRepository readersRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, ReadersRepository readersRepository) {
        this.booksRepository = booksRepository;
        this.readersRepository = readersRepository;
    }

    public List<Book> showAllSorted(int page, int booksPerPage){
        return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
    }

    public List<Book> showAll(int page, int booksPerPage){
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> showAllJustSorted() {
        return booksRepository.findAll(Sort.by("year"));
    }

    public List<Book> showAll(){
        return booksRepository.findAll();
    }

    public Book showByIndex(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void insert(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void setBookToReader(Reader reader, int bookId) {
        Book book = booksRepository.findById(bookId).orElse(null);
        Reader reader1 = readersRepository.findById(reader.getId()).orElse(null);
        if (reader1.getBooks().size() == 0){
            reader1.setBooks(new ArrayList<>(Collections.singletonList(book)));
        } else {
            reader1.getBooks().add(book);
        }
        book.setReader(reader1);
        booksRepository.save(book);
        readersRepository.save(reader1);
    }

    public Book showBookByReferenceId(int id) {
        Reader reader = readersRepository.findById(id).orElse(null);
        return booksRepository.findByReader(reader).stream().findAny().orElse(null);
    }


    public List<Book> showAllBooksByReferenceId(int id) {
        Reader reader = readersRepository.findById(id).orElse(null);
        return booksRepository.findByReader(reader);
    }

    @Transactional
    public void removeBookFromReader(int id) {
        Book book = booksRepository.findById(id).orElse(null);
        Reader reader = book.getReader();

        book.setReader(null);

        List<Book> books = reader.getBooks();

        books.remove(book);

        booksRepository.save(book);
        readersRepository.save(reader);
    }

    public Book findByNameStartingWith(String partOfNameOfTheBook){
        return booksRepository.findByNameStartingWith(partOfNameOfTheBook).get(0);
    }
}