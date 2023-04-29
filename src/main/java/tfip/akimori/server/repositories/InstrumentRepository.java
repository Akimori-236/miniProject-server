package tfip.akimori.server.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tfip.akimori.server.models.Instrument;
import tfip.akimori.server.models.User;

@Repository
public class InstrumentRepository {

        private final String SQL_GETINSTRUMENTSBYEMAIL = """
                        SELECT instrument_id, section, brand, model, serial_number, store_name, firstname, lastname
                        FROM instruments i
                        INNER JOIN store s
                        ON i.store_id = s.store_id
                        INNER JOIN users u
                        ON i.user_id = u.user_id
                        WHERE u.email=?;
                                """;
        private final String SQL_GETSTOREBYEMAIL = """
                        SELECT instrument_id, section, brand, model, serial_number, store_name, u2.firstname, u2.lastname, u2.email
                        FROM instruments i
                        INNER JOIN store s
                        ON i.store_id = s.store_id
                        INNER JOIN users u
                        ON s.user_id = u.user_id
                        INNER JOIN users u2
                        ON i.user_id = u2.user_id
                        WHERE u.email=?;
                                """;

        @Autowired
        private JdbcTemplate template;

        public List<Instrument> getBorrowedByEmail(String email) {
                List<Instrument> instruments = template.query(
                                SQL_GETINSTRUMENTSBYEMAIL,
                                (rs, rowNum) -> {
                                        User user = User.builder()
                                                        .firstname(rs.getString("firstname"))
                                                        .lastname(rs.getString("lastname"))
                                                        .email(email)
                                                        .build();
                                        Instrument instrument = Instrument.builder()
                                                        .instrument_id(rs.getInt("instrument_id"))
                                                        .brand(rs.getString("brand"))
                                                        .model(rs.getString("model"))
                                                        .serial_number(rs.getString("serial_number"))
                                                        .store_name(rs.getString("store_name"))
                                                        .user(user)
                                                        .build();
                                        return instrument;
                                }, email);

                return instruments;
        }

        public List<Instrument> getManagedInstrumentsByEmail(String email) {
                List<Instrument> instruments = template.query(
                                SQL_GETSTOREBYEMAIL,
                                (rs, rowNum) -> {
                                        User user = User.builder()
                                                        .firstname(rs.getString("firstname"))
                                                        .lastname(rs.getString("lastname"))
                                                        .email(rs.getString("email"))
                                                        .build();
                                        Instrument instrument = Instrument.builder()
                                                        .instrument_id(rs.getInt("instrument_id"))
                                                        .brand(rs.getString("brand"))
                                                        .model(rs.getString("model"))
                                                        .serial_number(rs.getString("serial_number"))
                                                        .store_name(rs.getString("store_name"))
                                                        .user(user)
                                                        .build();
                                        return instrument;
                                }, email);

                return instruments;
        }
}
