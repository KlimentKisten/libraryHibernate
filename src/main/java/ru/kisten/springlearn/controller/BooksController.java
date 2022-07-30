package ru.kisten.springlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kisten.springlearn.models.Book;
import ru.kisten.springlearn.models.Reader;
import ru.kisten.springlearn.services.BooksService;
import ru.kisten.springlearn.services.ReadersService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final ReadersService readersService;

    @Autowired
    public BooksController(BooksService booksService, ReadersService readersService) {
        this.booksService = booksService;
        this.readersService = readersService;
    }

    @GetMapping
    public String showBooks(Model model, HttpServletRequest request){

        boolean existingOfAmountOfBooks = request.getParameterMap().containsKey("books_per_page");
        boolean existingOfSortedByYear = request.getParameterMap().containsKey("sort_by_year");

        boolean sortByYear = Boolean.parseBoolean(request.getParameter("sort_by_year"));

        if (existingOfAmountOfBooks && existingOfSortedByYear && sortByYear){
            int page = Integer.parseInt(request.getParameter("page"));
            int booksPerPage = Integer.parseInt(request.getParameter("books_per_page"));
            model.addAttribute("books", booksService.showAllSorted(page, booksPerPage));
        } else if (existingOfAmountOfBooks){
            int page = Integer.parseInt(request.getParameter("page"));
            int booksPerPage = Integer.parseInt(request.getParameter("books_per_page"));
            model.addAttribute("books", booksService.showAll(page, booksPerPage));
        } else if(sortByYear){
            model.addAttribute("books", booksService.showAllJustSorted());
        } else {
            model.addAttribute("books", booksService.showAll());
        }
        return "books/show";
    }

    @GetMapping("/{id}")
    public String showBookByIndex(@PathVariable("id") int id, Model model){

        Book book = booksService.showByIndex(id);

        Reader reader = book.getReader();

        model.addAttribute("readers", readersService.showAll());
        model.addAttribute("reader", reader);
        model.addAttribute("readerForList", new Reader());
        model.addAttribute("book", booksService.showByIndex(id));

        return "books/index";

    }

    @GetMapping("/new")
    public String inputBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping
    public String addBook(@ModelAttribute @Valid Book book, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "books/new";
        }

        booksService.insert(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.showByIndex(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("{id}")
    public String deleteReader(@PathVariable("id") int id){

        booksService.delete(id);

        return "redirect:/books";

    }

    @PatchMapping("/add/{id}")
    public String addBook(@ModelAttribute Reader reader, @PathVariable("id") int bookId){
        booksService.setBookToReader(reader, bookId);
        return "redirect:/books/" + bookId;
    }

    @GetMapping("/search")
    public String returnSearchPage(Model model){
        model.addAttribute("book", new Book());
        return "books/search";
    }

    @PostMapping("/search")
    public String findBookByName(@ModelAttribute(name = "bookFromSearch") Book book, Model model){

            String partOfNameOfTheBook = book.getNameNotFull();
            book = booksService.findByNameStartingWith(partOfNameOfTheBook);
            Reader reader = book.getReader();

            model.addAttribute("book", book);
            model.addAttribute("reader", reader);


        return "books/search";
    }
}