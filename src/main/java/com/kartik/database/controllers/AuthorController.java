package com.kartik.database.controllers;


import com.kartik.database.domain.dto.AuthorDto;
import com.kartik.database.domain.entities.AuthorEntity;
import com.kartik.database.mappers.Mapper;
import com.kartik.database.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorService authorService;
    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper){
        this.authorService = authorService;
        this. authorMapper = authorMapper;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> create(@RequestBody AuthorDto author){
        AuthorEntity authorEntity = authorMapper.mapFrom(author); //take the DTO passed in from the function since springWeb will automatically convert JSON to java objects, so we need to convert java objects to java entity.
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity); //Once we have java entity, we can use that entity to then call the createAuthor method which will save the author to the database.
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED); //we will then return a DTO Author object back to the receiver.

    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors(){
        List<AuthorEntity> getAuthors= authorService.findAll();
        return getAuthors.stream().map(authorMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> find(@PathVariable Long id){
        Optional<AuthorEntity> findAuthor = authorService.findOne(id);
        return findAuthor.map(authorEntity -> {
            AuthorDto savedAuthorFound = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(savedAuthorFound, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto){

        if(authorService.isExist(id)){
            authorDto.setId(id);
            AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);

            AuthorEntity updatedAuthorEntity = authorService.save(authorEntity);
            AuthorDto updatedAuthorDto = authorMapper.mapTo(updatedAuthorEntity);

            return new ResponseEntity<> (updatedAuthorDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdate(@PathVariable Long id, @RequestBody AuthorDto authorDto){
        if(!authorService.isExist(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity partialUpdatedAuthorEntity = authorService.partialUpdate(id, authorEntity);
        AuthorDto savedUpdatedAuthorDto = authorMapper.mapTo(partialUpdatedAuthorEntity);

        return new ResponseEntity<>(savedUpdatedAuthorDto, HttpStatus.OK);
    }
}
