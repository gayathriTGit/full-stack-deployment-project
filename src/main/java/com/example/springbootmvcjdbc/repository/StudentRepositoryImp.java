package com.example.springbootmvcjdbc.repository;

import com.example.springbootmvcjdbc.model.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepositoryImp implements StudentRepository{

    @Autowired
    public JdbcTemplate jdbcTemplate;

    private static final RowMapper<Student> STUDENT_ROW_MAPPER = (rs, rowNum) ->{
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setEmail(rs.getString("email"));
        student.setAge(rs.getInt("age"));
        student.setMajor(rs.getString("major"));
        return student;
    };

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * from students ORDER BY id";
        return jdbcTemplate.query(sql, STUDENT_ROW_MAPPER);
    }

    @Override
    public Student findById(Long id) {
        String sql = "SELECT * from students WHERE id = ?";
        List<Student> students = jdbcTemplate.query(sql, STUDENT_ROW_MAPPER, id);
        return  students.isEmpty() ? null : students.get(0);
    }

    @Override
    public Student add(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, email, age, major) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setInt(4, student.getAge());
            ps.setString(5, student.getMajor());
            return ps;
        }, keyHolder);

        student.setId(keyHolder.getKey().longValue());
        return student;
    }

    @Override
    public Student update(Student student) {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, age = ?, major = ? WHERE id = ?";
        jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(),
                student.getEmail(), student.getAge(), student.getMajor(), student.getId());
        return student;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM students WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
