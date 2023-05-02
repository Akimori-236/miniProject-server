package tfip.akimori.server.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class StoreRepository implements SQLQueries {

    @Autowired
    private JdbcTemplate template;

    public Integer insertStore(String store_name) throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, store_name);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key.intValue();

    }

    public boolean insertStoreManager(String email, int store_id) throws SQLException {
        int rowsInserted = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANAGER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setInt(2, store_id);
            return ps;
        });
        return rowsInserted > 0;
    }
}
