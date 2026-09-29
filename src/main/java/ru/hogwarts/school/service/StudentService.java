package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.info("Was invoked method for find student");
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.error("No student with id={}", id);
        }
        return student;
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find students by age");
        logger.debug("Searching students with age={}", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find students by age between");
        logger.debug("Searching students with age between {} and {}", min, max);
        if (min > max) {
            logger.warn("Minimum age {} is greater than maximum age {}", min, max);
        }
        return studentRepository.findByAgeBetween(min, max);
    }

    public long countStudents() {
        logger.info("Was invoked method for count students");
        return studentRepository.countStudents();
    }

    public double findAverageAge() {
        logger.info("Was invoked method for find average age");
        Double averageAge = studentRepository.findAverageAge();
        if (averageAge == null) {
            logger.warn("There are no students, average age is 0.0");
            return 0.0;
        }
        return averageAge;
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Was invoked method for find last five students");
        return studentRepository.findLastFiveStudents();
    }

    public List<String> getNamesStartingWithA() {
        logger.info("Was invoked method for get names starting with A");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeByStream() {
        logger.info("Was invoked method for get average age by stream");
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    public int calculateParallelSum() {
        logger.info("Was invoked method for calculate parallel sum");
        return java.util.stream.Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }

    public Faculty getFacultyOfStudent(Long studentId) {
        logger.info("Was invoked method for get faculty of student");
        Student student = findStudent(studentId);
        if (student == null) {
            logger.error("No faculty can be found because student with id={} does not exist", studentId);
            return null;
        }
        return student.getFaculty();
    }
}
