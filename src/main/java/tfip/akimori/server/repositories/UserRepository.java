package tfip.akimori.server.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tfip.akimori.server.exceptions.DuplicateEmailException;
import tfip.akimori.server.models.User;

@Repository
public class UserRepository {

    private final String SQL_FINDBYEMAIL = "SELECT * FROM users WHERE email=?";
    private final String SQL_INSERTUSER = "INSERT INTO users(firstname, lastname, email, password, role) VALUES (?, ?, ?, ?, ?)";

    @Autowired
    private JdbcTemplate template;

    public Integer save(User user) throws DuplicateEmailException {
        System.out.println("PASSWORD>>>>>>>>" + user.getPassword());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERTUSER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getFirstname());
                ps.setString(2, user.getLastname());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getPassword());
                ps.setString(5, user.getRole().name());
                return ps;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            System.err.println(e);
            throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists", e);
        }
        Number key = keyHolder.getKey();
        return (key == null) ? null : key.intValue();
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = template.queryForObject(SQL_FINDBYEMAIL, BeanPropertyRowMapper.newInstance(User.class), email);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

}
