package com.kartik.database.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kartik.database.TestDataUtil;
import com.kartik.database.domain.entities.BookEntity;
import com.kartik.database.services.BookService;
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
public class BookControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private BookService bookService;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, BookService bookService){
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();

    }


    @Test
    public void testThatCreateBookReturnsTheCorrectHttpStatus() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        testBookA.setAuthorEntity(null);

        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/978-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }
    @Test
    public void testThatCreateBookReturnsTheCorrectJson() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        testBookA.setAuthorEntity(null);

        String bookJson = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/978-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("978-1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("The Shadow in the Attic")
        );
    }

    @Test
    public void testThatFindBookReturnsTheCorrectHttpStatus() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindBookReturnsCorrectJSON() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.save(testBookA.getIsbn(), testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value("9342")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value("The Shadow")
        );
    }

    @Test
    public void testThatFindOneBookReturnsTheCorrectHttpStatus() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/9342")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindOneBookReturnsTheCorrectHttpStatus404NotFound() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/90908")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindOneBookReturnCorrectJSONObject() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/9342")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("9342")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("The Shadow")
        );
    }


    @Test
    public void testThatUpdatingBookReturnsCorrectObject0() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.save(testBookA.getIsbn(), testBookA);

        BookEntity testBookB = TestDataUtil.createTestBookB(null);
        testBookB.setIsbn(testBookA.getIsbn());
        testBookB.setTitle("MockTest");

        String bookJson = objectMapper.writeValueAsString(testBookB);



        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookB.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookB.getTitle())
        );
    }

    @Test
    public void testThatUpdatingBookReturnsCorrectHttpStatus() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(null);
        bookService.save(testBookA.getIsbn(), testBookA);

        BookEntity testBookB = TestDataUtil.createTestBookB(null);
        testBookB.setIsbn(testBookA.getIsbn());
        testBookB.setTitle("MockTest");

        String bookJson = objectMapper.writeValueAsString(testBookB);



        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


}
