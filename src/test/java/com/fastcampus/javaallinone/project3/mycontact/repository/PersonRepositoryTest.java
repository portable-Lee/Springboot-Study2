package com.fastcampus.javaallinone.project3.mycontact.repository;

import com.fastcampus.javaallinone.project3.mycontact.domain.Person;
import com.fastcampus.javaallinone.project3.mycontact.domain.dto.Birthday;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class PersonRepositoryTest {    // JpaRepository를 상속받아 사용하면 문제가 없으나 새로 Query문을 만들어 사용할 때는 검증이 필요

    @Autowired
    private PersonRepository personRepository;

    @Test
    void findByName() {
        List<Person> people = personRepository.findByName("tony");
        assertThat(people.size()).isEqualTo(1);

        Person person = people.get(0);
        assertAll(() -> assertThat(person.getName()).isEqualTo("tony"),
                  () -> assertThat(person.getHobby()).isEqualTo("reading"),
                  () -> assertThat(person.getAddress()).isEqualTo("Seoul"),
                  () -> assertThat(person.getBirthday()).isEqualTo(Birthday.of(LocalDate.of(1991, 7, 10))),
                  () -> assertThat(person.getJob()).isEqualTo("officer"),
                  () -> assertThat(person.getPhoneNumber()).isEqualTo("010-2222-5555"),
                  () -> assertThat(person.isDeleted()).isEqualTo(false)
                 );
    }

    @Test
    void findByNameIfDeleted() {
        List<Person> people = personRepository.findByName("andrew");
        assertThat(people.size()).isEqualTo(0);
    }

    @Test
    void findByMonthOfBirthday() {
        List<Person> people = personRepository.findByMonthOfBirthday(7);
        assertThat(people.size()).isEqualTo(2);

        assertAll(() -> assertThat(people.get(0).getName()).isEqualTo("david"),
                  () -> assertThat(people.get(1).getName()).isEqualTo("tony")
                 );
    }

    @Test
    void findPeopleDeleted() {
        List<Person> people = personRepository.findPeopleDeleted();

        assertThat(people.size()).isEqualTo(1);
        assertThat(people.get(0).getName()).isEqualTo("andrew");
    }



    /************** birthday-friends **************/
    @Test
    void findBirthdayBetweenTodayAndTomorrow() {
        List<Person> people = personRepository.findBirthdayBetweenTodayAndTomorrow(Birthday.of(LocalDate.now()).getMonthOfBirthday(), Birthday.of(LocalDate.now()).getDayOfBirthday());

        assertThat(people.size()).isEqualTo(4);

        assertAll(() -> assertThat(people.get(0).getName()).isEqualTo("tom"),
                  () -> assertThat(people.get(1).getName()).isEqualTo("tom2"),
                  () -> assertThat(people.get(2).getName()).isEqualTo("tom4"),
                  () -> assertThat(people.get(3).getName()).isEqualTo("tom5")
                 );
    }
    /**********************************************/

}