package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.exception.PersonAlreadyExistsException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnErrorFromServiceWhenPersonDoesNotExists() throws Exception {
        when(personDataService.findPerson(any(), any())).thenThrow(PersonNotFoundException.class);
        this.mockMvc.perform(get("/person/smith/mary1"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnErrorWhenNoPersonExistsWithSameLastname() throws Exception {
        when(personDataService.getAllPersonsWithSameSurname(any())).thenThrow(PersonNotFoundException.class);
        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnListWhenMultiplePersonExistsWithSameLastname() throws Exception {
        List<Person> personList = Arrays.asList(
                new Person("Collin", "Brown"),
                new Person("Ian", "Brown")
        );
        when(personDataService.getAllPersonsWithSameSurname("smith")).thenReturn(personList);
        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Collin"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[1].firstName").value("Ian"))
                .andExpect(jsonPath("$[1].lastName").value("Brown"));
    }

    @Test
    public void shouldReturnListWhenSinglePersonExistsWithSameLastname() throws Exception {
        List<Person> personList = Arrays.asList(
                new Person("Collin", "Brown")
        );
        when(personDataService.getAllPersonsWithSameSurname("smith")).thenReturn(personList);
        this.mockMvc.perform(get("/person/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Collin"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"));
    }

    @Test
    public void shouldReturnSuccessWhenPersonIsAdded() throws Exception {
        doNothing().when(personDataService).addPerson("smith", "brown");
        this.mockMvc.perform(post("/person/smith/wills"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorWhenPersonAlreadyExists() throws Exception {
        doThrow(PersonAlreadyExistsException.class).doNothing().when(personDataService).addPerson(any(), any());
        this.mockMvc.perform(post("/person/smith/wills"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}