package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method for find faculty");
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            logger.error("No faculty with id={}", id);
        }
        return faculty;
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByColor(String color) {
        logger.info("Was invoked method for find faculties by color");
        logger.debug("Searching faculties with color={}", color);
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> searchByNameOrColor(String search) {
        logger.info("Was invoked method for search faculties by name or color");
        logger.debug("Searching faculties with value={}", search);
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(search, search);
    }

    public String getLongestFacultyName() {
        logger.info("Was invoked method for get longest faculty name");
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .filter(java.util.Objects::nonNull)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    public Collection<Student> getStudentsOfFaculty(Long facultyId) {
        logger.info("Was invoked method for get students of faculty");
        Faculty faculty = findFaculty(facultyId);
        if (faculty == null) {
            logger.error("No students can be found because faculty with id={} does not exist", facultyId);
            return null;
        }
        return faculty.getStudents();
    }
}
