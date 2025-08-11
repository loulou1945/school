package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student by ID");
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Long id, Student student) {
        logger.info("Was invoked method for edit student");
        Student existingStudent = studentRepository.findById(id).orElse(null);
        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student by ID");
        studentRepository.deleteById(id);
    }

    public Collection<Student> findStudentByAge(int age) {
        logger.info("Was invoked method for find student by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find student by age between");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Long getCountAllStudents() {
        logger.info("Was invoked method for get count all students");
        return studentRepository.getCountAllStudents();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average age");
        return studentRepository.getAverageAge();
    }

    public List<Student> getFiveLastStudents() {
        logger.info("Was invoked method for get five last students");
        return studentRepository.getFiveLastStudents();
    }

}
