package com.kartik.database.repositories;


import com.kartik.database.TestDataUtil;
import com.kartik.database.domain.entities.AuthorEntity;
import com.kartik.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {


    private BookRepository underTests;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTests) {
        this.underTests = underTests;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        BookEntity bookEntity = TestDataUtil.createTestBookA(authorEntity);

        underTests.save(bookEntity);
        Optional<BookEntity> results = underTests.findById(bookEntity.getIsbn());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();

        BookEntity bookEntityA = TestDataUtil.createTestBookA(authorEntity);

        underTests.save(bookEntityA);

        BookEntity bookEntityB = TestDataUtil.createTestBookB(authorEntity);

        underTests.save(bookEntityB);

        BookEntity bookEntityC = TestDataUtil.createTestBookC(authorEntity);

        underTests.save(bookEntityC);


        Iterable<BookEntity> results = underTests.findAll();
        assertThat(results).hasSize(3);
        assertThat(results).contains(bookEntityA, bookEntityB, bookEntityC);

    }

    @Test
    public void testThatBookCanBeCreatedAndUpdated(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();

        BookEntity bookEntityA = TestDataUtil.createTestBookA(authorEntity);

        underTests.save(bookEntityA);

        bookEntityA.setTitle("UPDATED");

        underTests.save(bookEntityA);
        Optional<BookEntity> results = underTests.findById(bookEntityA.getIsbn());

        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(bookEntityA);
    }

    @Test
    public void testThatBookCanBeCreatedAndDeleted(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();

        BookEntity bookEntityA = TestDataUtil.createTestBookA(authorEntityA);

        underTests.save(bookEntityA);

        underTests.deleteById(bookEntityA.getIsbn());

        Optional<BookEntity> results = underTests.findById(bookEntityA.getIsbn());
        assertThat(results).isEmpty();


    }

}
