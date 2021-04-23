package com.fastcampus.javaallinone.project3.mycontact.repository;

import com.fastcampus.javaallinone.project3.mycontact.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByName(String name);   // where person = ?

    @Query(value = "select person from Person person where person.birthday.monthOfBirthday = :monthOfBirthday")   // ?1 = int monthOfBirthday, ?2 = int dayOfBirthday / @Param("변수명")으로 가져온 값을 :변수명 에 대입 / nativeQuery
    List<Person> findByMonthOfBirthday(@Param("monthOfBirthday") int monthOfBirthday);

    @Query(value = "select * from Person person where person.deleted = true", nativeQuery = true)   // nativeQuery : 작성된 query문으로 실행
    List<Person> findPeopleDeleted();

}
