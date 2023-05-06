package tfip.akimori.server.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tfip.akimori.server.models.Store;
import tfip.akimori.server.models.User;

@Repository
public class StoreRepository implements SQLQueries {

    @Autowired
    private JdbcTemplate template;

    public Integer createStore(String store_name) throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_STORE, Statement.RETURN_GENERATED_KEYS);
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

    public List<String> getManagedStores(String email) {
        return template.queryForList(SQL_GETMANAGEDSTORES, String.class, email);
    }

    public List<Store> getStoreManagers(String email) {
        List<Store> storeList = template.query(
                SQL_GETSTOREMANAGERS, (rs, rowNum) -> {
                    return storeBuilder(rs);
                }, email);
        return storeList;
    }

    private static Store storeBuilder(ResultSet rs) {
        User user;
        try {
            user = User.builder()
                    .givenname(rs.getString("givenname"))
                    .familyname(rs.getString("familyname"))
                    .email(rs.getString("email"))
                    .build();
        } catch (SQLException e) {
            System.err.println("FAILED BUILDING USER");
            return null;
        }
        Store store;
        try {
            store = Store.builder()
                    .store_name(rs.getString("store_name"))
                    .user(user)
                    .build();

        } catch (SQLException e) {
            System.err.println("FAILED BUILDING STORE");
            return null;
        }
        return store;
    }
}
