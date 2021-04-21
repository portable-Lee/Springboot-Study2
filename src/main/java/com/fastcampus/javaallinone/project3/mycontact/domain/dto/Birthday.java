package com.fastcampus.javaallinone.project3.mycontact.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Birthday {

//    private static LocalDate today = LocalDate.now();     // 23시 59분에 객체가 생성되고 API가 다음날 0시에 반환되면 문제가 발생할 수 있음

    private Integer yearOfBirthday;

    private Integer monthOfBirthday;

    private Integer dayOfBirthday;

    private Birthday(LocalDate birthday) {   // 각 월과 일을 맞춤 ex)1월 : 31일, 2월 : 28일, ...
        this.yearOfBirthday = birthday.getYear();
        this.monthOfBirthday = birthday.getMonthValue();
        this.dayOfBirthday = birthday.getDayOfMonth();
    }

    public static Birthday of(LocalDate birthday) {
        return new Birthday(birthday);
    }

}
