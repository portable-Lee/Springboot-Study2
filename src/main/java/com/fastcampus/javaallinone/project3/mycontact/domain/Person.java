package com.fastcampus.javaallinone.project3.mycontact.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor    // 필요한 인자(@NonNull 로 표시)를 가지고 생성하는 생성자
@Data                       // @Getter, @Setter, @ToString, @EqualsAndHashCode(override하여 사용할 경우 오류가 발생할 수 있으므로 제공되는 어노테이션 사용) 모두 포함
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Integer age;

    private String hobby;

    @NonNull
    private String bloodType;

    private String address;

    private LocalDate birthday;

    private String job;

    @ToString.Exclude   // 데이터 숨김
    private String phoneNumber;

//    public boolean equals(Object object) {
//
//        if (object == null) {
//            return false;
//        }
//
//        Person person = (Person) object;
//
//        if (!person.getName().equals(this.getName())) {
//            return false;
//        }
//
//        if (person.getAge() != this.getAge()) {
//            return false;
//        }
//
//        return true;
//    }
//
//    public int hashCode() {
//        return (name + age).hashCode();
//    }

}
