package tfip.akimori.server.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tfip.akimori.server.models.Store;

@Repository
public class StoreRepository implements SQLQueries {

    @Autowired
    private JdbcTemplate template;

    public boolean createStore(String store_id, String store_name, String creator_email) throws SQLException {
        // KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("NEW STORE ID: " + store_id);
        int rowsInserted = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_STORE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, store_id);
            ps.setString(2, store_name);
            ps.setString(3, creator_email);
            return ps;
        }
        // , keyHolder
        );
        // Number key = keyHolder.getKey();
        if (rowsInserted != 1) {
            throw new SQLException("Error inserting into stores table");
        }
        return rowsInserted == 1;

    }

    public boolean insertStoreManager(String email, String store_id) throws SQLException {
        int rowsInserted = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_MANAGER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, store_id);
            return ps;
        });
        if (rowsInserted != 1) {
            throw new SQLException("Error inserting into managers table");
        }
        return rowsInserted == 1;
    }

    public List<Store> getManagedStores(String email) {
        return template.query(SQL_GETMANAGEDSTORES,
                BeanPropertyRowMapper.newInstance(Store.class),
                email);
    }

    public Boolean isManagerOfStore(String email, String storeID) {
        return 1 == template.update(SQL_CHECK_ISMANAGEROFSTORE, storeID, email);
    }

    // public List<Store> getStoreDetails(Integer store_id) {
    // List<Store> storeList = template.query(
    // SQL_GETSTOREMANAGERS, (rs, rowNum) -> {
    // return storeBuilder(rs);
    // }, store_id);
    // return storeList;
    // }

    // private static Store storeBuilder(ResultSet rs) {
    // Store store;
    // try {
    // store = Store.builder()
    // .store_id(rs.getInt("store_id"))
    // .store_name(rs.getString("store_name"))
    // .build();

    // } catch (SQLException e) {
    // System.err.println("FAILED BUILDING STORE");
    // return null;
    // }
    // return store;
    // }
}
