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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void createFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Cat");
        faculty.setColor("gray");

        ResponseEntity<Faculty> response = testRestTemplate.postForEntity(
                getAddress(),
                faculty,
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
    }

    @Test
    public void getFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Dog");
        faculty.setColor("black");

        facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
                getAddress() + "/" + facultyRepository.findAll().get(0).getId().toString(),
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("student")
                .isEqualTo(faculty);
    }


    @Test
    public void editFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Frog");
        faculty.setColor("green");

        facultyRepository.save(faculty);

        faculty.setName("Phoenix");
        faculty.setColor("orange");

        String url = getAddress() + "/" + faculty.getId();

        HttpEntity<Faculty> request = new HttpEntity<>(faculty);

        ResponseEntity<Faculty> response = testRestTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("student")
                .isEqualTo(faculty);
    }

    @Test
    public void deleteFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        facultyRepository.save(faculty);

        ResponseEntity<Faculty> responseDelete = testRestTemplate.exchange(
                getAddress() + "/" + facultyRepository.findAll().get(0).getId().toString(),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseDelete.getBody()).isNull();
    }

    @Test
    public void filterFacultyByColorOrNameTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Tit");
        faculty.setColor("blue");

        Faculty faculty1 = new Faculty();
        faculty1.setName("Swallow");
        faculty1.setColor("white");

        facultyRepository.save(faculty);
        facultyRepository.save(faculty1);

        ResponseEntity<List<Faculty>> responseFilter = testRestTemplate.exchange(
                getAddress() + "/color-or-name?color=white&name=Tit",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {
                }
        );

        assertThat(responseFilter.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseFilter.getBody()).isNotNull();
        assertThat(responseFilter.getBody().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFields("student")
                .isEqualTo(faculty);
    }

    @Test
    public void getStudentsByFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("green");
        faculty = facultyRepository.save(faculty);

        Student student1 = new Student();
        student1.setName("Leo");
        student1.setAge(1);
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setName("Nina");
        student2.setAge(2);
        student2.setFaculty(faculty);

        studentRepository.save(student1);
        studentRepository.save(student2);

        faculty.setStudents(new HashSet<>(studentRepository.findAll()));

        ResponseEntity<Collection<Student>> response = testRestTemplate.exchange(
                getAddress() + "/" + faculty.getId() + "/student",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        Collection<Student> students = response.getBody();
        assertThat(students).hasSize(2);

        List<Student> expectedStudents = Arrays.asList(student1, student2);

        students.forEach(actualStudent -> {
            boolean found = expectedStudents.stream()
                    .anyMatch(expected ->
                            expected.getName().equals(actualStudent.getName()) &&
                            expected.getAge() == actualStudent.getAge()
                    );
            assertThat(found).isTrue();
        });

    }

    private String getAddress() {
        return "http://localhost:" + port + "/faculty";
    }

    @AfterEach
    public void resetDb() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

}