package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.model.Faculty;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(MockedBeansConfig.class)
public class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getStudentByIdTest() throws Exception {
        String name = "testName";
        int age = 1;
        long id = 1L;

        Student student = new Student();
        student.setId(id);
        student.setAge(age);
        student.setName(name);

        when(studentService.findStudent(id)).thenReturn(student);

        mockMvc.perform(get("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void createStudentTest() throws Exception {
        String name = "name";
        int age = 1;
        long id = 1L;

        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setId(id);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("id", id);
        studentMap.put("name", name);
        studentMap.put("age", age);

        String jsonRequest = objectMapper.writeValueAsString(studentMap);

        mockMvc.perform(post("/student")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void editStudentTest() throws Exception {
        String name = "name";
        int age = 1;
        long id = 1L;

        Student student = new Student();
        student.setId(id);
        student.setAge(age);
        student.setName(name);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("id", id);
        studentMap.put("name", name);
        studentMap.put("age", age);

        String jsonRequest = objectMapper.writeValueAsString(studentMap);

        mockMvc.perform(post("/student")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));

        String nameUpdate = "nameUpdate";
        int ageUpdate = 12;

        Student updateStudent = new Student();
        updateStudent.setName(nameUpdate);
        updateStudent.setAge(ageUpdate);
        updateStudent.setId(id);

        when(studentService.editStudent(id, updateStudent)).thenReturn(updateStudent);

        Map<String, Object> studentMapUp = new HashMap<>();
        studentMapUp.put("id", id);
        studentMapUp.put("name", nameUpdate);
        studentMapUp.put("age", ageUpdate);

        String jsonRequestUp = objectMapper.writeValueAsString(studentMapUp);

        mockMvc.perform(put("/student/" + id)
                        .content(jsonRequestUp)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(nameUpdate))
                .andExpect(jsonPath("$.age").value(ageUpdate));
    }

    @Test
    public void deleteStudentTest() throws Exception {
        String name = "testName";
        int age = 12;
        long id = 1L;

        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setId(id);

        when(studentService.findStudent(any(Long.class))).thenReturn(student);

        mockMvc.perform(get("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));

        doNothing().when(studentService).deleteStudent(id);

        mockMvc.perform(delete("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(id);

        when(studentService.findStudent(id)).thenReturn(null);
        mockMvc.perform(get("/student/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findStudentByAgeTest() throws Exception {
        String name = "name";
        int age = 11;
        long id = 1L;

        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setId(id);

        when(studentService.findStudentByAge(anyInt())).thenReturn(List.of(student));

        mockMvc.perform(get("/student/age/" + age))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void findStudentByAgeBetweenTest() throws Exception {
        String name = "name";
        int age = 11;
        long id = 1L;

        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setId(id);

        when(studentService.findByAgeBetween(anyInt(), anyInt())).thenReturn(List.of(student));

        mockMvc.perform(get("/student/age-between?min=10&max=12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void getFacultyByStudentTest() throws Exception {

        String nameFaculty = "faculty";
        String color = "color";
        long idFaculty = 1L;

        Faculty faculty = new Faculty();
        faculty.setId(idFaculty);
        faculty.setName(nameFaculty);
        faculty.setColor(color);

        String nameStudent = "student";
        int age = 12;
        long idStudent = 2L;

        Student student = new Student();
        student.setId(idStudent);
        student.setName(nameStudent);
        student.setAge(age);
        student.setFaculty(faculty);

        when(studentService.findStudent(idStudent)).thenReturn(student);

        mockMvc.perform(get("/student/" + idStudent + "/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(nameFaculty))
                .andExpect(jsonPath("$.color").value(color));


    }
}
