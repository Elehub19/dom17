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
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentControllerTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StudentService studentService;

    @Test
    void createStudent() {
        Student student = new Student(1L, "Harry Potter", 17);
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = restTemplate.postForEntity("/student", student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
    }

    @Test
    void getStudent() {
        Student student = new Student(1L, "Harry Potter", 17);
        when(studentService.findStudent(1L)).thenReturn(student);

        ResponseEntity<Student> response = restTemplate.getForEntity("/student/1", Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
    }

    @Test
    void getStudentNotFound() {
        when(studentService.findStudent(999L)).thenReturn(null);

        ResponseEntity<String> response = restTemplate.getForEntity("/student/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getAllStudents() {
        when(studentService.getAllStudents()).thenReturn(List.of(
                new Student(1L, "Harry Potter", 17),
                new Student(2L, "Ron Weasley", 17)
        ));

        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void editStudent() {
        Student student = new Student(1L, "Harry Potter Updated", 18);
        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = restTemplate.exchange(
                "/student", HttpMethod.PUT, new HttpEntity<>(student), Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter Updated");
    }

    @Test
    void editStudentNotFound() {
        Student student = new Student(999L, "Unknown", 20);
        when(studentService.editStudent(any(Student.class))).thenReturn(null);

        ResponseEntity<String> response = restTemplate.exchange(
                "/student", HttpMethod.PUT, new HttpEntity<>(student), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteStudent() {
        doNothing().when(studentService).deleteStudent(1L);

        ResponseEntity<String> response = restTemplate.exchange(
                "/student/1", HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
        verify(studentService).deleteStudent(1L);
    }

    @Test
    void getStudentsByAge() {
        when(studentService.findByAge(17)).thenReturn(List.of(
                new Student(1L, "Harry Potter", 17)
        ));

        ResponseEntity<Student[]> response = restTemplate.getForEntity("/student/age/17", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getAge()).isEqualTo(17);
    }

    @Test
    void getStudentsByAgeBetween() {
        when(studentService.findByAgeBetween(16, 18)).thenReturn(List.of(
                new Student(1L, "Harry Potter", 17),
                new Student(2L, "Ron Weasley", 18)
        ));

        ResponseEntity<Student[]> response =
                restTemplate.getForEntity("/student/age?min=16&max=18", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void getStudentFaculty() {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        Student student = new Student(1L, "Harry Potter", 17);
        student.setFaculty(faculty);
        when(studentService.getFacultyOfStudent(1L)).thenReturn(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/student/1/faculty", Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
    }
}
