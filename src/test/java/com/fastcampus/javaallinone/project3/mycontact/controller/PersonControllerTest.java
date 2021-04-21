package com.fastcampus.javaallinone.project3.mycontact.controller;

import com.fastcampus.javaallinone.project3.mycontact.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class PersonControllerTest {

    @Autowired
    private PersonController personController;

    @Autowired
    private PersonRepository personRepository;

    private MockMvc mockMvc;

    @BeforeEach     // test 실행 전에 먼저 실행됨
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    @Order(1)
    void getPerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/person/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void postPerson() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                                .content("{\n"
                                                        + "\"name\": \"martin2\",\n"
                                                        + "\"age\": 20,\n"
                                                        + "\"bloodType\": \"A\"\n"
                                                        + "}"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    void modifyPerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/person/1")
                                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                                .content("{\n"
                                                        + "\"name\": \"martin\",\n"
                                                        + "\"age\": 20,\n"
                                                        + "\"bloodType\": \"A\"\n"
                                                        + "}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void modifyName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/person/1").param("name", "martin22"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @Disabled
    void deletePerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/person/1")).andDo(print()).andExpect(status().isOk());

//        log.info("people deleted : {}", personRepository.findPeopleDeleted());
        System.out.println("people deleted : " + personRepository.findPeopleDeleted());
    }

}