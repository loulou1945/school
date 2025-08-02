select student.name, student.age, faculty.name
from student
left join faculty on student.faculty_id = faculty.id

select student.name, student.age
from avatar
left join student on avatar.student_id = student.id