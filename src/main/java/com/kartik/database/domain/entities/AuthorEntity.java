package com.kartik.database.domain.entities;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="authors")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq") //this is specifiying that the ID is the id of the entity and this will generate a new incremental ID when we don't pas is anything.
    private Long id;

    private String name;

    private Integer age;

}
