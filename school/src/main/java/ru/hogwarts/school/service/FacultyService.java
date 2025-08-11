package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty by ID");
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Long id, Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        Faculty existingFaculty = facultyRepository.findById(id).orElse(null);
        existingFaculty.setName(faculty.getName());
        existingFaculty.setColor(faculty.getColor());
        return facultyRepository.save(existingFaculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty by ID");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findFacultyByColorOrName(String color, String name) {
        logger.info("Was invoked method for find faculty by color or name");
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

}
