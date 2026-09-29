package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.*;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FacultyControllerTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private FacultyService facultyService;

    @Test
    void createFaculty() {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", faculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
        assertThat(response.getBody().getColor()).isEqualTo("Red");
    }

    @Test
    void getFaculty() {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/1", Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
    }

    @Test
    void getFacultyNotFound() {
        when(facultyService.findFaculty(999L)).thenReturn(null);

        ResponseEntity<String> response = restTemplate.getForEntity("/faculty/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getAllFaculties() {
        when(facultyService.getAllFaculties()).thenReturn(List.of(
                new Faculty(1L, "Gryffindor", "Red"),
                new Faculty(2L, "Slytherin", "Green")
        ));

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("/faculty", Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void editFaculty() {
        Faculty faculty = new Faculty(1L, "Gryffindor Updated", "Scarlet");
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty", HttpMethod.PUT, new HttpEntity<>(faculty), Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor Updated");
    }

    @Test
    void editFacultyNotFound() {
        Faculty faculty = new Faculty(999L, "Unknown", "Black");
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(null);

        ResponseEntity<String> response = restTemplate.exchange(
                "/faculty", HttpMethod.PUT, new HttpEntity<>(faculty), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteFaculty() {
        doNothing().when(facultyService).deleteFaculty(1L);

        ResponseEntity<String> response = restTemplate.exchange(
                "/faculty/1", HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
        verify(facultyService).deleteFaculty(1L);
    }

    @Test
    void getFacultiesByColor() {
        when(facultyService.findByColor("Red")).thenReturn(List.of(
                new Faculty(1L, "Gryffindor", "Red")
        ));

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("/faculty/color/Red", Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getColor()).isEqualTo("Red");
    }

    @Test
    void searchFaculties() {
        when(facultyService.searchByNameOrColor("red")).thenReturn(List.of(
                new Faculty(1L, "Gryffindor", "Red")
        ));

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "/faculty/filter?search=red", Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Gryffindor");
    }

    @Test
    void getFacultyStudents() {
        List<Student> students = List.of(new Student(1L, "Harry Potter", 17));
        when(facultyService.getStudentsOfFaculty(1L)).thenReturn(students);

        ResponseEntity<Student[]> response =
                restTemplate.getForEntity("/faculty/1/students", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Harry Potter");
    }
}
