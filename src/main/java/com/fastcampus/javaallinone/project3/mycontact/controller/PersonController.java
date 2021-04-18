package com.fastcampus.javaallinone.project3.mycontact.controller;

import com.fastcampus.javaallinone.project3.mycontact.controller.dto.PersonDto;
import com.fastcampus.javaallinone.project3.mycontact.domain.Person;
import com.fastcampus.javaallinone.project3.mycontact.repository.PersonRepository;
import com.fastcampus.javaallinone.project3.mycontact.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/person")
@RestController
@Slf4j
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id){
        return personService.getPerson(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)     // http response 상태 코드 201을 반환
    public void postPerson(@RequestBody Person person) {
        personService.put(person);

        log.info("person -> {}", personRepository.findAll());
    }

    @PutMapping("/{id}")    // 전체 update
    public void modifyPerson(@PathVariable Long id, @RequestBody PersonDto person) {
        personService.modify(id, person);

        log.info("person -> {}", personRepository.findAll());
    }

    @PatchMapping("/{id}")  // 일부만 update
    public void modifyPerson(@PathVariable Long id, String name) {
        personService.modify(id, name);

        log.info("person -> {}", personRepository.findAll());
    }

}
