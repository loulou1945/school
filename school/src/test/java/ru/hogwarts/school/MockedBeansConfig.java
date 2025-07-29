package ru.hogwarts.school;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

@TestConfiguration
public class MockedBeansConfig {
    @Bean
    public StudentService studentService() {
        return Mockito.mock(StudentService.class);
    }

    @Bean
    public FacultyService facultyService() {
        return Mockito.mock(FacultyService.class);
    }
}