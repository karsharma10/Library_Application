package com.kartik.database.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private String isbn;

    private String title;
    private AuthorDto authorDto;  //now we can have the Author in the book to be mapped to the author object instead now.

}
