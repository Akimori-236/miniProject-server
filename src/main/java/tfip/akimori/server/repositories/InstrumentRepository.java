package tfip.akimori.server.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                                SQL_GETBORROWEDBYEMAIL,
                                (rs, rowNum) -> {
                                        return instrumentBuilder(rs);
                                }, email);

                return instruments;
        }

        public List<Instrument> getManagedInstrumentsByEmail(String email) {
                List<Instrument> instruments = template.query(
                                SQL_GETMANAGEDINSTRUMENTSBYEMAIL,
                                (rs, rowNum) -> {
                                        return instrumentBuilder(rs);
                                }, email);

                return instruments;
        }

        public boolean insertInstrument(Instrument instr, int store_id) {
                int rowsInserted = template.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_INSTRUMENT,
                                        Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, instr.getType());
                        ps.setString(2, instr.getBrand());
                        ps.setString(3, instr.getModel());
                        ps.setString(4, instr.getSerial_number());
                        ps.setInt(5, store_id);
                        return ps;
                });
                return rowsInserted > 0;
        }

        private Instrument instrumentBuilder(ResultSet rs) {
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
