package com.fastcampus.javaallinone.project3.mycontact.domain;

import com.fastcampus.javaallinone.project3.mycontact.controller.dto.PersonDto;
import com.fastcampus.javaallinone.project3.mycontact.domain.dto.Birthday;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor    // 필요한 인자(@NonNull 로 표시)를 가지고 생성하는 생성자
@Data                       // @Getter, @Setter, @ToString, @EqualsAndHashCode(override하여 사용할 경우 오류가 발생할 수 있으므로 제공되는 어노테이션 사용) 모두 포함
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NonNull
    @Min(1)
    private Integer age;

    private String hobby;

    @NonNull
    @NotEmpty
    @Column(nullable = false)
    private String bloodType;

    private String address;

    @Embedded
    @Valid
    private Birthday birthday;

    private String job;

    @ToString.Exclude   // 데이터 숨김
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)   // block을 했는지 안했는지만 확인하면 되기 때문에 OneToOne   / fetch, optional
    @ToString.Exclude
    private Block block;

    public void set(PersonDto personDto) {
        if (personDto.getAge() != 0) {
            this.setAge(personDto.getAge());
        }

        if (!StringUtils.isEmpty(personDto.getHobby())) {
            this.setHobby(personDto.getHobby());
        }

        if (!StringUtils.isEmpty(personDto.getBloodType())) {
            this.setBloodType(personDto.getBloodType());
        }

        if (!StringUtils.isEmpty(personDto.getAddress())) {
            this.setAddress(personDto.getAddress());
        }

        if (!StringUtils.isEmpty(personDto.getJob())) {
            this.setJob(personDto.getJob());
        }

        if (!StringUtils.isEmpty(personDto.getPhoneNumber())) {
            this.setPhoneNumber(personDto.getPhoneNumber());
        }
    }

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
