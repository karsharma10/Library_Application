package com.kartik.database;

import com.kartik.database.domain.entities.AuthorEntity;
import com.kartik.database.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil(){

    }


    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("Abiligal Rose")
                .age(80)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Thomas Cronin")
                .age(44)
                .build();
    }
    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Jese A Casey")
                .age(24)
                .build();
    }

    public static BookEntity createTestBookA(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1")
                .title("The Shadow in the Attic")
                .authorEntity(authorEntity)
                .build();
    }
    public static BookEntity createTestBookB(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-121")
                .title("Beyond the Horizon")
                .authorEntity(authorEntity)
                .build();
    }
    public static BookEntity createTestBookC(final AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1432")
                .title("THe Last Ember")
                .authorEntity(authorEntity)
                .build();
    }
}
