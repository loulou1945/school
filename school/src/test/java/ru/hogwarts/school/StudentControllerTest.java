package ru.hogwarts.school;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Test
    public void createStudentTest() {
        Student student = new Student();
        student.setName("Harry");
        student.setAge(1);

        ResponseEntity<Student> response = testRestTemplate.postForEntity(
                getAddress(),
                student,
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
    }

    @Test
    public void getStudentTest() {
        Student student = new Student();
        student.setName("Malfoy");
        student.setAge(1);

        studentRepository.save(student);

        ResponseEntity<Student> response = testRestTemplate.getForEntity(
                getAddress() + "/" + studentRepository.findAll().get(0).getId().toString(),
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
    }

    @Test
    public void editStudentTest() {
        Student student = new Student();
        student.setName("Vika");
        student.setAge(1);

        studentRepository.save(student);

        student.setName("Sveta");
        student.setAge(2);

        String url = getAddress() + "/" + student.getId();

        HttpEntity<Student> request = new HttpEntity<>(student);

        ResponseEntity<Student> response = testRestTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
    }

    @Test
    public void deleteStudentTest() {
        Student student = new Student();
        student.setName("Vika");
        student.setAge(1);

        studentRepository.save(student);

        ResponseEntity<Student> responseDelete = testRestTemplate.exchange(
                getAddress() + "/" + studentRepository.findAll().get(0).getId().toString(),
                HttpMethod.DELETE,
                null,
                Student.class
        );

        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseDelete.getBody()).isNull();
    }

    @Test
    public void filterStudentByAgeTest() {
        Student student = new Student();
        student.setName("Vika");
        student.setAge(1);

        Student student1 = new Student();
        student1.setName("Olga");
        student1.setAge(5);

        studentRepository.save(student);
        studentRepository.save(student1);

        ResponseEntity<List<Student>> responseFilter = testRestTemplate.exchange(
                getAddress() + "/age/5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(responseFilter.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseFilter.getBody()).isNotNull();
        assertThat(responseFilter.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student1);
    }

    @Test
    public void filterStudentByAgeBetweenTest() {
        Student student = new Student();
        student.setName("Vika");
        student.setAge(1);

        Student student1 = new Student();
        student1.setName("Lera");
        student1.setAge(5);

        studentRepository.save(student);
        studentRepository.save(student1);

        ResponseEntity<List<Student>> responseFilter = testRestTemplate.exchange(
                getAddress() + "/age-between?min=4&max=6",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );

        assertThat(responseFilter.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseFilter.getBody()).isNotNull();
        assertThat(responseFilter.getBody().size()).isEqualTo(1);
        assertThat(responseFilter.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student1);
    }

    @Test
    public void getFacultyByStudentTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Cat");
        faculty.setColor("orange");

        Student student = new Student();
        student.setName("Vika");
        student.setAge(1);
        student.setFaculty(faculty);

        facultyRepository.save(faculty);
        studentRepository.save(student);

        ResponseEntity<Faculty> responseGetFaculty = testRestTemplate.getForEntity(
                getAddress() + "/" + studentRepository.findAll().get(0).getId().toString() + "/faculty",
                Faculty.class
        );

        assertThat(responseGetFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseGetFaculty.getBody()).isNotNull();
        assertThat(responseGetFaculty.getBody())
                .isEqualTo(faculty);
    }

    private String getAddress() {
        return "http://localhost:" + port + "/student";
    }

    @AfterEach
    public void resetDb() {
        studentRepository.deleteAll();
    }
}