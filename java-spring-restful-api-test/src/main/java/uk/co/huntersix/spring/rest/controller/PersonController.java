package uk.co.huntersix.spring.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@Slf4j
@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<String> person(@PathVariable(value = "lastName") String lastName,
                                         @PathVariable(value = "firstName") String firstName) {
        Person person = personDataService.findPerson(lastName, firstName);
        return new ResponseEntity(person, HttpStatus.OK);
    }

    @GetMapping("/person/{lastName}")
    public ResponseEntity<List<Person>> allPersonsWithSameSurname(@PathVariable(value = "lastName") String lastName) {
        List<Person> person = personDataService.getAllPersonsWithSameSurname(lastName);
        ResponseEntity<List<Person>> response = new ResponseEntity<>(person, HttpStatus.OK);
        return response;
    }

    @PostMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<String> addPerson(@PathVariable(value = "lastName") String lastName,
                                            @PathVariable(value = "firstName") String firstName) {
        personDataService.addPerson(lastName, firstName);
        return ResponseEntity.ok("Person Added");
    }

}