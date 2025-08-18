package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("{id}")
    public ResponseEntity<Student> editStudent(@PathVariable Long id, @RequestBody Student student) {
        Student foundStudent = studentService.editStudent(id, student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("age/{age}")
    public ResponseEntity<Collection<Student>> findStudentByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.findStudentByAge(age));
    }

    @GetMapping("age-between")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam int min, @RequestParam int max) {
        if (min > max || min == max) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudent(@PathVariable Long id) {
        Student foundStudent = studentService.findStudent(id);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent.getFaculty());
    }

    @GetMapping("count-students")
    public ResponseEntity<Long> getCountAllStudents() {
        return ResponseEntity.ok(studentService.getCountAllStudents());
    }

    @GetMapping("average-age")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("five-last-students")
    public ResponseEntity<List<Student>> getFiveLastStudents() {
        return ResponseEntity.ok(studentService.getFiveLastStudents());
    }

    @GetMapping("name-start-with-A")
    public ResponseEntity<List<String>> getAllStudentsNameStartWithA() {
        return ResponseEntity.ok(studentService.getAllStudentsNameStartWithA());
    }

    @GetMapping("average-age-stream")
    public ResponseEntity<Double> getAverageAgeStream() {
        return ResponseEntity.ok(studentService.getAverageAgeStream());
    }

    @GetMapping("print-parallel")
    public ResponseEntity<Void> printNameParallel() {
        studentService.printNameParallel();
        return ResponseEntity.ok().build();
    }

    @GetMapping("print-synchronized")
    public ResponseEntity<Void> printNameSynchronized() {
        studentService.printNameSynchronized();
        return ResponseEntity.ok().build();
    }
}
