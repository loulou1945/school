package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getAllStudentsNameStartWithA() {
        logger.info("Was invoked method for get all students name start with A");

        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(e -> e.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .toList();

    }

    public Double getAverageAgeStream() {
        logger.info("Was invoked method for get average age by stream");

        return studentRepository.findAll().stream()
                .collect(Collectors.averagingDouble(Student::getAge));
    }

    public void printNameParallel() {
        logger.info("Was invoked parallel method for print students' names");

        List<Student> studentList = studentRepository.findAll();

        System.out.println(studentList.get(0).getName());
        System.out.println(studentList.get(1).getName());

        new Thread(() -> {
            System.out.println(studentList.get(2).getName());
            System.out.println(studentList.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println(studentList.get(4).getName());
            System.out.println(studentList.get(5).getName());
        }).start();
    }


    public void printNameSynchronized() {
        logger.info("Was invoked synchronized method for print students' names");

        List<Student> studentList = studentRepository.findAll();

        printName(studentList, 0);
        printName(studentList, 1);

        new Thread(() -> {
            printName(studentList, 2);
            printName(studentList, 3);
        }).start();

        new Thread(() -> {
            printName(studentList, 4);
            printName(studentList, 5);
        }).start();
    }

    private synchronized void printName(List<Student> students, int i) {
        System.out.println(students.get(i).getName());
    }
}
