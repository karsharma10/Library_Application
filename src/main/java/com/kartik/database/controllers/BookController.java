package com.kartik.database.controllers;


import com.kartik.database.domain.dto.BookDto;
import com.kartik.database.domain.entities.BookEntity;
import com.kartik.database.mappers.Mapper;
import com.kartik.database.services.impl.BookServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private Mapper<BookEntity, BookDto> bookMapper;
    private BookServiceImpl bookService;

    public BookController(Mapper<BookEntity, BookDto> bookMapper, BookServiceImpl bookService){
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){
        if(bookService.isExists(isbn)){

            BookEntity updateBookEntity = bookMapper.mapFrom(bookDto);
            BookEntity savedUpdatedEntity = bookService.save(isbn, updateBookEntity);
            BookDto savedUpdatedDto = bookMapper.mapTo(savedUpdatedEntity);

            return new ResponseEntity<>(savedUpdatedDto, HttpStatus.OK);
        }
        //if it doesn't exist then we are creating a new book:
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity savedResults = bookService.save(isbn, bookEntity);

        return new ResponseEntity<>(bookMapper.mapTo(savedResults), HttpStatus.CREATED);

    }

    @GetMapping(path = "/books")
    public List<BookDto> listBooks(){
        List<BookEntity> getBooks = bookService.findAll();
        return getBooks.stream().map(bookMapper::mapTo).collect(Collectors.toList()); //a way to take a list of Book entities and convert to list of bookDTOs
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> find(@PathVariable String isbn){
        Optional<BookEntity> findBooks = bookService.findOne(isbn);

        return findBooks.map(bookEntity -> {
            BookDto findBooksDTO = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(findBooksDTO, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
