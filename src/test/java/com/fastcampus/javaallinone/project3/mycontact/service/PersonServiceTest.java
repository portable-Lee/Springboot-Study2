package com.fastcampus.javaallinone.project3.mycontact.service;

import com.fastcampus.javaallinone.project3.mycontact.controller.dto.PersonDto;
import com.fastcampus.javaallinone.project3.mycontact.domain.Person;
import com.fastcampus.javaallinone.project3.mycontact.domain.dto.Birthday;
import com.fastcampus.javaallinone.project3.mycontact.exception.PersonNotFoundException;
import com.fastcampus.javaallinone.project3.mycontact.exception.RenameNotPermittedException;
import com.fastcampus.javaallinone.project3.mycontact.repository.PersonRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)     // Mockito를 사용하는 이유 : 해당 test에 대해 더 자세히 검증 가능, SpringBootTest보다 더 빠르게 test 가능하므로 logic이 많을수록 더 편리함
class PersonServiceTest {   // 해당 class에서 ctrl + shift + t 단축기를 누르면 Test Class 생성 가능

    @InjectMocks    // 해당 test의 class(현재 PersonServiceTest 이므로 PersonService에 사용)
    private PersonService personService;

    @Mock           // Autowired 하는 class
    private PersonRepository personRepository;

    @Test
    void getAll() {
        when(personRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Lists.newArrayList(new Person("martin"), new Person("dennis"), new Person("tony"))));

        Page<Person> result = personService.getAll(PageRequest.of(0, 3));   // Pageable Interface를 구현한 PageRequest 사용

        assertThat(result.getNumberOfElements()).isEqualTo(3);
        assertThat(result.getContent().get(0).getName()).isEqualTo("martin");
        assertThat(result.getContent().get(1).getName()).isEqualTo("dennis");
        assertThat(result.getContent().get(2).getName()).isEqualTo("tony");
    }

    @Test
    void getPeopleByName() {
        when(personRepository.findByName("martin")).thenReturn(Lists.newArrayList(new Person("martin")));   // if문과 같으나 실제로 실행되는것이 아니라 실행되었다고 가정함
                                                                                                                  // PersonRepository에서 Query문을 변경하여도 오류 없이 정상적으로 실행함

        List<Person> result = personService.getPeopleByName("martin");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("martin");
    }

    @Test
    void getPerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person("martin")));

        Person person = personService.getPerson(1L);

        assertThat(person.getName()).isEqualTo("martin");
    }

    @Test
    void getPersonIfNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        Person person = personService.getPerson(1L);

        assertThat(person).isNull();
    }

    @Test
    void put() {
        personService.put(mockPersonDto());     // return 값이 없을때는 호출이 성공 또는 실패하였는지에 대해서만 검증

        verify(personRepository, times(1)).save(argThat(new IsPersonWillBeInserted()));     // personService.put(dto)를 호출하여 실제로 personRepository.save(dto)가 실행되는지에 대한 검증
    }

    @Test
    void modifyIfPersonNotFound() {     // modify에 대한 test는 분기별로 검증이 필요(1. id의 존재)
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.modify(1L, mockPersonDto()));
    }

    @Test
    void modifyIfNameIsDifferent() {    // modify에 대한 test는 분기별로 검증이 필요(2. name의 일치)
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person("tony")));

        assertThrows(RenameNotPermittedException.class, () -> personService.modify(1L, mockPersonDto()));
    }

    @Test
    void modify() {     // modify에 대한 test는 분기별로 검증이 필요(3. modify가 정상 실행되었는지)
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person("martin")));

        personService.modify(1L, mockPersonDto());

        verify(personRepository, times(1)).save(argThat(new IsPersonWillBeUpdated()));  // personDto가 정상적으로 person 객체에 set 되었는지 확인
    }

    @Test
    void modifyByNameIfPersonNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.modify(1L, "daniel"));
    }

    @Test
    void modifyByName() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person("martin")));

        personService.modify(1L, "daniel");

        verify(personRepository, times(1)).save(argThat(new IsPersonNameWillBeUpdated()));
    }

    @Test
    void deleteIfPersonNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.delete(1L));
    }

    @Test
    void delete() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person("martin")));

        personService.delete(1L);

        verify(personRepository, times(1)).save(argThat(new IsPersonWillBeDeleted()));  // delete가 아닌 save인 이유 : PersonService의 delete method에서 data 삭제 과정을 변경하였기 때문(deleted flag를 사용하여 true/false로 check)
    }



    /************** birthday-friends **************/
    @Test
    void getBirthdayFriends() {
        when(personRepository.findBirthdayBetweenTodayAndTomorrow(LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth())).thenReturn(Lists.newArrayList(new Person("tom"), new Person("tom2"), new Person("tom4"), new Person("tom5")));

        List<Person> result = personService.getBirthdayFriends();

        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0).getName()).isEqualTo("tom");
        assertThat(result.get(1).getName()).isEqualTo("tom2");
        assertThat(result.get(2).getName()).isEqualTo("tom4");
        assertThat(result.get(3).getName()).isEqualTo("tom5");
    }
    /**********************************************/



    private PersonDto mockPersonDto() {
        return PersonDto.of("martin", "programming", "판교", LocalDate.now(), "programmer", "010-1111-2222");
    }

    private static class IsPersonWillBeInserted implements ArgumentMatcher<Person> {

        @Override
        public boolean matches(Person person) {
            return equals(person.getName(), "martin")
                    && equals(person.getHobby(), "programming")
                    && equals(person.getAddress(),"판교")
                    && equals(person.getBirthday(), Birthday.of(LocalDate.now()))
                    && equals(person.getJob(), "programmer")
                    && equals(person.getPhoneNumber(), "010-1111-2222");
        }

        private boolean equals(Object actual, Object expected) {
            return expected.equals(actual);
        }
    }

    private static class IsPersonWillBeUpdated implements ArgumentMatcher<Person> {

        @Override
        public boolean matches(Person person) {
            return equals(person.getName(), "martin")
                   && equals(person.getHobby(), "programming")
                   && equals(person.getAddress(),"판교")
                   && equals(person.getBirthday(), Birthday.of(LocalDate.now()))
                   && equals(person.getJob(), "programmer")
                   && equals(person.getPhoneNumber(), "010-1111-2222");
        }

        private boolean equals(Object actual, Object expected) {
            return expected.equals(actual);
        }

    }

    private static class IsPersonNameWillBeUpdated implements ArgumentMatcher<Person> {

        @Override
        public boolean matches(Person person) {
            return person.getName().equals("daniel");
        }
    }

    private static class IsPersonWillBeDeleted implements ArgumentMatcher<Person> {

        @Override
        public boolean matches(Person person) {
            return person.isDeleted();
        }
    }

}