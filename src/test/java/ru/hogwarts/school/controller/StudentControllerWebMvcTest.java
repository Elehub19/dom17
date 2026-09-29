package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void createStudent() throws Exception {
        Student student = new Student(1L, "Harry Potter", 17);
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"));
    }

    @Test
    void getStudent() throws Exception {
        when(studentService.findStudent(1L))
                .thenReturn(new Student(1L, "Harry Potter", 17));

        mockMvc.perform(get("/student/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void getStudentNotFound() throws Exception {
        when(studentService.findStudent(999L)).thenReturn(null);

        mockMvc.perform(get("/student/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void getAllStudents() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(
                new Student(1L, "Harry Potter", 17),
                new Student(2L, "Ron Weasley", 17)
        ));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"));
    }

    @Test
    void editStudent() throws Exception {
        Student student = new Student(1L, "Harry Updated", 18);
        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Updated"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void editStudentNotFound() throws Exception {
        Student student = new Student(999L, "Unknown", 20);
        when(studentService.editStudent(any(Student.class))).thenReturn(null);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void deleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/student/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(studentService).deleteStudent(1L);
    }

    @Test
    void getStudentsByAge() throws Exception {
        when(studentService.findByAge(17)).thenReturn(List.of(
                new Student(1L, "Harry Potter", 17)
        ));

        mockMvc.perform(get("/student/age/{age}", 17))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void getStudentsByAgeBetween() throws Exception {
        when(studentService.findByAgeBetween(16, 18)).thenReturn(List.of(
                new Student(1L, "Harry Potter", 17),
                new Student(2L, "Ron Weasley", 18)
        ));

        mockMvc.perform(get("/student/age")
                        .param("min", "16")
                        .param("max", "18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getStudentFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        when(studentService.getFacultyOfStudent(1L)).thenReturn(faculty);

        mockMvc.perform(get("/student/{id}/faculty", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }
}
