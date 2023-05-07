package tfip.akimori.server.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tfip.akimori.server.models.Instrument;

@Repository
public class InstrumentRepository implements SQLQueries {

    @Autowired
    private JdbcTemplate template;

    public List<Instrument> getBorrowedByEmail(String email) {
        return template.query(SQL_GETBORROWEDBYEMAIL,
                BeanPropertyRowMapper.newInstance(Instrument.class),
                email);
    }

    public List<Instrument> getManagedInstrumentsByEmail(String email) {
        return template.query(SQL_GETMANAGEDINSTRUMENTSBYEMAIL,
                BeanPropertyRowMapper.newInstance(Instrument.class),
                email);
    }

    public boolean insertInstrument(Instrument instr, int store_id) {
        int rowsInserted = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_INSTRUMENT,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, instr.getInstrument_type());
            ps.setString(2, instr.getBrand());
            ps.setString(3, instr.getModel());
            ps.setString(4, instr.getSerial_number());
            ps.setInt(5, store_id);
            return ps;
        });
        return rowsInserted > 0;
    }

}