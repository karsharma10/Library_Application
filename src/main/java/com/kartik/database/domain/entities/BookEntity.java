package com.kartik.database.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "books")

public class BookEntity {

    @Id
    private String isbn;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL) //when we retrieve the author from the database, if we make changes to the author when we retrieve it, those changes will be consistent.
    @JoinColumn(name = "author_id")
    private AuthorEntity authorEntity;  //now we can have the Author in the book to be mapped to the author object instead now.

}
