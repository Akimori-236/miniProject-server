package tfip.akimori.server.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tfip.akimori.server.models.Instrument;
import tfip.akimori.server.models.User;

@Repository
public class InstrumentRepository implements SQLQueries {

        @Autowired
        private JdbcTemplate template;

        public List<Instrument> getBorrowedByEmail(String email) {
                List<Instrument> instruments = template.query(
                                SQL_GETINSTRUMENTSBYEMAIL,
                                (rs, rowNum) -> {
                                        return instrumentBuilder(rs);
                                }, email);

                return instruments;
        }

        public List<Instrument> getManagedInstrumentsByEmail(String email) {
                List<Instrument> instruments = template.query(
                                SQL_GETSTOREBYEMAIL,
                                (rs, rowNum) -> {
                                        return instrumentBuilder(rs);
                                }, email);

                return instruments;
        }

        private Instrument instrumentBuilder(ResultSet rs) {
                User user;
                try {
                        user = User.builder()
                                        .firstname(rs.getString("firstname"))
                                        .lastname(rs.getString("lastname"))
                                        .email(rs.getString("email"))
                                        .build();
                } catch (SQLException e) {
                        System.err.println("FAILED BUILDING USER");
                        return null;
                }
                Instrument instrument;
                try {
                        instrument = Instrument.builder()
                                        .instrument_id(rs.getInt("instrument_id"))
                                        .type(rs.getString("instrument_type"))
                                        .brand(rs.getString("brand"))
                                        .model(rs.getString("model"))
                                        .serial_number(rs.getString("serial_number"))
                                        .store_name(rs.getString("store_name"))
                                        .user(user)
                                        .build();
                } catch (SQLException e) {
                        System.err.println("FAILED BUILDING INSTRUMENT");
                        return null;
                }
                return instrument;
        }
}
