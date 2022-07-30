package ru.kisten.springlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kisten.springlearn.models.Reader;
import ru.kisten.springlearn.services.BooksService;
import ru.kisten.springlearn.services.ReadersService;

import javax.validation.Valid;

@Controller
@RequestMapping("/readers")
public class ReadersController {

    private final ReadersService readersService;
    private final BooksService booksService;

    @Autowired
    public ReadersController(ReadersService readersService, BooksService booksService) {
        this.readersService = readersService;
        this.booksService = booksService;
    }


    @GetMapping
    public String showReaders(Model model){

        model.addAttribute("readers", readersService.showAll());

        return "readers/show";
    }

    @GetMapping("{id}")
    public String showReaderByIndex(@PathVariable("id") int id, Model model){

        model.addAttribute("book", booksService.showBookByReferenceId(id));
        model.addAttribute("books", booksService.showAllBooksByReferenceId(id));
        model.addAttribute("reader", readersService.showByIndex(id));

        return "readers/index";
    }

    @GetMapping("/new")
    public String inputReader(Model model){

        model.addAttribute("reader", new Reader());

        return "readers/new";
    }

    @PostMapping
    public String addReader(@ModelAttribute @Valid Reader reader, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "readers/new";
        }

        readersService.insert(reader);

        return "redirect:/readers";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model){

        model.addAttribute("reader", readersService.showByIndex(id));

        return "readers/edit";

    }

    @PatchMapping("/{id}")
    public String updateReader(@PathVariable("id") int id, @ModelAttribute @Valid Reader reader, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "readers/edit";
        }

        readersService.update(id, reader);

        return "redirect:/readers";

    }

    @DeleteMapping("{id}")
    public String deleteReader(@PathVariable("id") int id){

        readersService.delete(id);

        return "redirect:/readers";

    }

    @PatchMapping("/remove/{id}")
    public String removeBookFromReader(@PathVariable("id") int id){
        booksService.removeBookFromReader(id);
        return "redirect:/books/" + id;
    }
}