package com.fastcampus.javaallinone.project3.mycontact.controller;

import com.fastcampus.javaallinone.project3.mycontact.controller.dto.PersonDto;
import com.fastcampus.javaallinone.project3.mycontact.domain.Person;
import com.fastcampus.javaallinone.project3.mycontact.domain.dto.Birthday;
import com.fastcampus.javaallinone.project3.mycontact.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class PersonControllerTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach     // @Test 실행 전에 먼저 실행됨
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                                 .addFilters(new CharacterEncodingFilter("UTF-8", true))    // MockHttpServletResponse 부분에서 한글이 깨져서 추가함
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/person")
                                              .param("page", "1")           // 몇번째 page인지
                                              .param("size", "2"))          // 한 page마다 몇 개의 elements를 가져오는지
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(3))       // 전체 page 수
                .andExpect(jsonPath("$.totalElements").value(6))    // 전체 elements 갯수
                .andExpect(jsonPath("$.numberOfElements").value(2)) // page별 elements 갯수
//                .andExpect(jsonPath("$.content.[0].name").value("martin"))  // $.[x].name : 객체의 index값에 해당하는 name값을 가져옴
//                .andExpect(jsonPath("$.content.[1].name").value("david"))
//                .andExpect(jsonPath("$.content.[2].name").value("dennis"))
//                .andExpect(jsonPath("$.content.[3].name").value("sophia"))
//                .andExpect(jsonPath("$.content.[4].name").value("benny"))
//                .andExpect(jsonPath("$.content.[5].name").value("tony"));
                .andExpect(jsonPath("$.content.[0].name").value("dennis"))
                .andExpect(jsonPath("$.content.[1].name").value("sophia"));
    }

    @Test
    @Order(1)
    void getPerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/person/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("martin"))  // $ : 객체, $.name과 같이 각 객체의 attribute 값을 가져올 수 있음, chaining 사용 가능 / value는 가져온 값이 맞는지 확인
               .andExpect(jsonPath("$.hobby").isEmpty())
               .andExpect(jsonPath("$.address").isEmpty())
               .andExpect(jsonPath("$.birthday").value("1991-08-15"))
               .andExpect(jsonPath("$.job").isEmpty())
               .andExpect(jsonPath("$.phoneNumber").isEmpty())
               .andExpect(jsonPath("$.deleted").value(false))
               .andExpect(jsonPath("$.age").isNumber())
               .andExpect(jsonPath("$.birthdayToday").isBoolean());
    }

    @Test
    @Order(2)
    void postPerson() throws Exception {
        PersonDto dto = PersonDto.of("martin", "programming", "판교", LocalDate.now(), "programmer", "010-1111-2222");

         mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                                               .contentType(MediaType.APPLICATION_JSON_UTF8)
                                               .content(toJsonString(dto)))
                .andExpect(status().isCreated());

        Person result = personRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0);

        assertAll(() -> assertThat(result.getName()).isEqualTo("martin"),
                  () -> assertThat(result.getHobby()).isEqualTo("programming"),
                  () -> assertThat(result.getAddress()).isEqualTo("판교"),
                  () -> assertThat(result.getBirthday()).isEqualTo(Birthday.of(LocalDate.now())),
                  () -> assertThat(result.getJob()).isEqualTo("programmer"),
                  () -> assertThat(result.getPhoneNumber()).isEqualTo("010-1111-2222")
                 );
    }

    @Test
    void postPersonIfNameIsNull() throws Exception {
        PersonDto dto = new PersonDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                                              .contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(toJsonString(dto)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value(400))
               .andExpect(jsonPath("$.message").value("이름은 필수값입니다."));
    }

    @Test
    void postPersonIfNameIsEmptyString() throws Exception {
        PersonDto dto = new PersonDto();
        dto.setName("");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                                              .contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(toJsonString(dto)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value(400))
               .andExpect(jsonPath("$.message").value("이름은 필수값입니다."));
    }

    @Test
    void postPersonIfNameIsBlankString() throws Exception {
        PersonDto dto = new PersonDto();
        dto.setName(" ");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                                              .contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(toJsonString(dto)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value(400))
               .andExpect(jsonPath("$.message").value("이름은 필수값입니다."));
    }

    @Test
    @Order(3)
    void modifyPerson() throws Exception {
        PersonDto dto = PersonDto.of("martin", "programming", "판교", LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/person/1")
                                              .contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(toJsonString(dto)))
               .andExpect(status().isOk());

        Person result = personRepository.findById(1L).get();

        // 한번에 모든 결과를 반환
        assertAll(() -> assertThat(result.getName()).isEqualTo("martin"),
                  () -> assertThat(result.getHobby()).isEqualTo("programming"),
                  () -> assertThat(result.getAddress()).isEqualTo("판교"),
                  () -> assertThat(result.getBirthday()).isEqualTo(Birthday.of(LocalDate.now())),
                  () -> assertThat(result.getJob()).isEqualTo("programmer"),
                  () -> assertThat(result.getPhoneNumber()).isEqualTo("010-1111-2222")
                 );
    }

    @Test
    void modifyPersonIfNameIsDifferent() throws Exception {
        PersonDto dto = PersonDto.of("james", "programming", "판교", LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/person/1")
                                              .contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(toJsonString(dto)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value(400))
               .andExpect(jsonPath("$.message").value("이름 변경이 허용되지 않습니다."));
    }

    @Test
    void modifyPersonIfPersonNotFound() throws Exception {
        PersonDto dto = PersonDto.of("martin", "programming", "판교", LocalDate.now(), "programmer", "010-1111-2222");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/person/20")
                                              .contentType(MediaType.APPLICATION_JSON_UTF8)
                                              .content(toJsonString(dto)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value(400))
               .andExpect(jsonPath("$.message").value("Person Entity가 존재하지 않습니다."));
    }

    @Test
    @Order(4)
    void modifyName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/person/1")
                                              .param("name", "martinModified"))
               .andExpect(status().isOk());

        assertThat(personRepository.findById(1L).get().getName()).isEqualTo("martinModified");
    }

    @Test
    @Order(5)
    void deletePerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/person/1"))
               .andExpect(status().isOk());

        assertTrue(personRepository.findPeopleDeleted().stream().anyMatch(person -> person.getId().equals(1L)));
    }



    /************** birthday-friends **************/
    @Test
    void getBirthdayFriends() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/person/birthday-friends"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.[0].name").value("tom"))
               .andExpect(jsonPath("$.[1].name").value("tom2"))
               .andExpect(jsonPath("$.[2].name").value("tom4"))
               .andExpect(jsonPath("$.[3].name").value("tom5"));
    }
    /**********************************************/



    private String toJsonString(PersonDto personDto) throws JsonProcessingException{
        return objectMapper.writeValueAsString(personDto);
    }

}