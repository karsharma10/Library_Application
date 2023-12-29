package com.kartik.database.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kartik.database.TestDataUtil;
import com.kartik.database.domain.entities.AuthorEntity;
import com.kartik.database.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {


    private AuthorService authorService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService){
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorReturnsSuccessfulHttp201Created() throws Exception {

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);

        String authorJson = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorReturnsSuccessfulSavedAuthor() throws Exception {

        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorA.setId(null);

        String authorJson = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abiligal Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("80")
        );
    }

    @Test
    public void testThatGetAuthorsReturnsSuccessfulSavedAuthor() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsListOfAuthors() throws Exception {

        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Abiligal Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value("80")
        );
    }

    @Test
    public void testThatGetAuthorsReturnsSuccessfulAuthor200() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsSuccessfulAuthor404WhenAuthorNotFound() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/100000")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorReturnsCorrectAuthor() throws Exception {

        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abiligal Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("80")
        );
    }
    @Test
    public void testThatFullUpdateAuthorReturnCorrectHTTPStatus200() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnCorrectHTTPStatus404WhenNoneExists() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnCorrectUpdatedAuthor() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setName("Testy123");
        String authorJson = objectMapper.writeValueAsString(testAuthorA);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/"+testAuthorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Testy123")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("80")
        );
    }


    @Test
    public void testThatUpdatingAuthorsReturnsTheCorrectHttpStatus() throws Exception {

        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setName("Testy123");
        authorService.save(testAuthorA);

        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+testAuthorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdatingAuthorsReturnsTheCorrectObject() throws Exception {

        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setName("Testy123");
        authorService.save(testAuthorA);

        String authorJson = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+testAuthorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testAuthorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Testy123")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorA.getAge())
        );
    }

    @Test
    public void testThatAuthorDeletedReturnsCorrectHttpStatus404() throws Exception {
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        authorService.save(testAuthorA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/"+testAuthorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }


}

