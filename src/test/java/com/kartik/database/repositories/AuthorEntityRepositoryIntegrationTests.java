package com.kartik.database.repositories;

import com.kartik.database.TestDataUtil;
import com.kartik.database.domain.entities.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {

    private AuthorRepository underTests;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTests){
        this.underTests = underTests;
    }
    @Test
    public void testThatAuthorCanBeCreatedAndRecalled(){

        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        underTests.save(authorEntity);
        Optional<AuthorEntity> result = underTests.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);

    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTests.save(authorEntityA);

        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        underTests.save(authorEntityB);

        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();
        underTests.save(authorEntityC);

        Iterable<AuthorEntity> results = underTests.findAll();
        assertThat(results).hasSize(3);
        assertThat(results).contains(authorEntityA, authorEntityB, authorEntityC);

    }

    @Test
    public void testThatAuthorCanBeCreatedAndUpdated(){
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        underTests.save(authorEntity);
        authorEntity.setName("UPDATED");
        underTests.save(authorEntity);

        Optional<AuthorEntity>results = underTests.findById(authorEntity.getId());
        assertThat(results).isPresent();
        assertThat(results.get()).isEqualTo(authorEntity);

    }

    @Test
    public void testThatAuthorCanBeCreatedAndDeleted(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTests.save(authorEntityA);

        underTests.deleteById(authorEntityA.getId());
        Optional<AuthorEntity> results = underTests.findById(authorEntityA.getId());

        assertThat(results).isEmpty();

    }

    @Test
    public void testThatGetAuthorsWithAgeLessThan(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();

        underTests.save(authorEntityA);
        underTests.save(authorEntityB);
        underTests.save(authorEntityC);

         Iterable<AuthorEntity> results = underTests.ageLessThan(50);

         assertThat(results).containsExactly(authorEntityB, authorEntityC);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan(){
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();

        underTests.save(authorEntityA);
        underTests.save(authorEntityB);
        underTests.save(authorEntityC);

        Iterable<AuthorEntity> results = underTests.findAuthorsWithAgeGreaterThan(50);
        assertThat(results).containsExactly(authorEntityA);
    }

}
