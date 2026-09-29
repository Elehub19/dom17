package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    @Test
    void createFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor", "Red");
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void getFaculty() throws Exception {
        when(facultyService.findFaculty(1L))
                .thenReturn(new Faculty(1L, "Gryffindor", "Red"));

        mockMvc.perform(get("/faculty/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void getFacultyNotFound() throws Exception {
        when(facultyService.findFaculty(999L)).thenReturn(null);

        mockMvc.perform(get("/faculty/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void getAllFaculties() throws Exception {
        when(facultyService.getAllFaculties()).thenReturn(List.of(
                new Faculty(1L, "Gryffindor", "Red"),
                new Faculty(2L, "Slytherin", "Green")
        ));

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));
    }

    @Test
    void editFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Gryffindor Updated", "Scarlet");
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor Updated"))
                .andExpect(jsonPath("$.color").value("Scarlet"));
    }

    @Test
    void editFacultyNotFound() throws Exception {
        Faculty faculty = new Faculty(999L, "Unknown", "Black");
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(null);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void deleteFaculty() throws Exception {
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(delete("/faculty/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(facultyService).deleteFaculty(1L);
    }

    @Test
    void getFacultiesByColor() throws Exception {
        when(facultyService.findByColor("Red")).thenReturn(List.of(
                new Faculty(1L, "Gryffindor", "Red")
        ));

        mockMvc.perform(get("/faculty/color/{color}", "Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    @Test
    void searchFaculties() throws Exception {
        when(facultyService.searchByNameOrColor("red")).thenReturn(List.of(
                new Faculty(1L, "Gryffindor", "Red")
        ));

        mockMvc.perform(get("/faculty/filter").param("search", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));
    }

    @Test
    void getFacultyStudents() throws Exception {
        when(facultyService.getStudentsOfFaculty(1L)).thenReturn(List.of(
                new Student(1L, "Harry Potter", 17)
        ));

        mockMvc.perform(get("/faculty/{id}/students", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"));
    }
}
