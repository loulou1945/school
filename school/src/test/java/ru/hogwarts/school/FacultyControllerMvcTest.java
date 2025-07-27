package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.service.FacultyService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
@Import(MockedBeansConfig.class)
public class FacultyControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getFacultyByIdTest() throws Exception {
        String name = "testName";
        String color = "testColor";
        Long facultyId = 1L;

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        faculty.setId(facultyId);

        when(facultyService.findFaculty(facultyId)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/" + facultyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void createFacultyTest() throws Exception {
        String name = "testName";
        String color = "testColor";
        Long facultyId = 1L;

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        faculty.setId(facultyId);

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        Map<String, Object> facultyMap = new HashMap<>();
        facultyMap.put("id", facultyId);
        facultyMap.put("name", name);
        facultyMap.put("color", color);

        String jsonRequest = objectMapper.writeValueAsString(facultyMap);

        mockMvc.perform(post("/faculty")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void editFacultyTest() throws Exception {
        String name = "testName";
        String color = "testColor";
        Long facultyId = 1L;

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        faculty.setId(facultyId);

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        Map<String, Object> facultyMap = new HashMap<>();
        facultyMap.put("id", facultyId);
        facultyMap.put("name", name);
        facultyMap.put("color", color);

        String jsonRequest = objectMapper.writeValueAsString(facultyMap);

        mockMvc.perform(post("/faculty")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        String nameUpdate = "updateName";
        String colorUpdate = "updateColor";

        Faculty updateFaculty = new Faculty();
        updateFaculty.setName(nameUpdate);
        updateFaculty.setColor(colorUpdate);
        updateFaculty.setId(facultyId);

        when(facultyService.editFaculty(facultyId, updateFaculty)).thenReturn(updateFaculty);

        Map<String, Object> facultyMapUp = new HashMap<>();
        facultyMapUp.put("id", facultyId);
        facultyMapUp.put("name", nameUpdate);
        facultyMapUp.put("color", colorUpdate);

        String jsonRequestUp = objectMapper.writeValueAsString(facultyMapUp);

        mockMvc.perform(put("/faculty/" + facultyId)
                        .content(jsonRequestUp)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(nameUpdate))
                .andExpect(jsonPath("$.color").value(colorUpdate));
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        String name = "testName";
        String color = "testColor";
        long facultyId = 1L;

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        faculty.setId(facultyId);

        when(facultyService.findFaculty(any(Long.class))).thenReturn(faculty);

        mockMvc.perform(get("/faculty/" + facultyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        doNothing().when(facultyService).deleteFaculty(facultyId);

        mockMvc.perform(delete("/faculty/" + facultyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).deleteFaculty(facultyId);

        when(facultyService.findFaculty(facultyId)).thenReturn(null);
        mockMvc.perform(get("/faculty/" + facultyId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getFacultyByColorOrNameTest() throws Exception {
        String name = "name";
        String color = "color";
        Long facultyId = 1L;

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        faculty.setId(facultyId);

        when(facultyService.findFacultyByColorOrName(anyString(), anyString())).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculty/color-or-name?color=1&name=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));
    }

    @Test
    public void getStudentByFacultyTest() throws Exception {

        String nameStudent = "testName";
        int age = 1;
        long studentId = 1L;

        Student student = new Student();
        student.setAge(age);
        student.setName(nameStudent);
        student.setId(studentId);

        String nameFaculty = "name";
        String color = "color";
        long facultyId = 1L;

        Faculty faculty = new Faculty();
        faculty.setName(nameFaculty);
        faculty.setColor(color);
        faculty.setId(facultyId);

        Set<Student> students = new HashSet<>();
        students.add(student);
        faculty.setStudent(students);

        when(facultyService.findFaculty(facultyId)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/" + facultyId + "/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(nameStudent))
                .andExpect(jsonPath("$[0].age").value(age));

    }
}