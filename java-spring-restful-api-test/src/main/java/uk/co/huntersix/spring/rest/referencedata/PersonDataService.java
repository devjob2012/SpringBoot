package uk.co.huntersix.spring.rest.referencedata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.exception.PersonAlreadyExistsException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonDataService {
    public static List<Person> PERSON_DATA = new ArrayList(Arrays.asList(
            new Person("Mary", "Smith"),
            new Person("Brian", "Archer"),
            new Person("Collin", "Brown"),
            new Person("Ian", "Brown"))
    );

    public Person findPerson(String lastName, String firstName) {

        List<Person> personList = PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        if (personList.isEmpty()) {
            log.warn("No Person with name : {} {}", firstName, lastName);
            throw new PersonNotFoundException("No person not found with given firstname and lastname");
        }
        return personList.get(0);
    }

    public List<Person> getAllPersonsWithSameSurname(String lastName) {
        List<Person> personList = PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        if (personList.isEmpty()) {
            log.warn("No Person with lastname :  {} ", lastName);
            throw new PersonNotFoundException("No person not found with given lastname");
        }
        return personList;
    }

    public void addPerson(String lastName, String firstName) {
        List<Person> personList = PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        if (!personList.isEmpty()) {
            log.warn("Person already exists : {} {} ", firstName, lastName);
            throw new PersonAlreadyExistsException("Person already exists with given firstname and lastname");
        }
        PERSON_DATA.add(new Person(firstName, lastName));
    }
}
