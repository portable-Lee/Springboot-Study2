package com.fastcampus.javaallinone.project3.mycontact.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PersonDto {

    @NotBlank(message = "이름은 필수값입니다.")  // Validation - @NotBlank를 통해 이름값에 null 또는 빈칸이 들어오지 못하게 함
    private String name;

    private String hobby;

    private String address;

    private LocalDate birthday;

    private String job;

    private String phoneNumber;

}
