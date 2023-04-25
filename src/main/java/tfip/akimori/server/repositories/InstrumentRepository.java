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
            SELECT brand, model, serial_number, store_name, firstname, lastname FROM instruments
            INNER JOIN store
            ON instruments.store_id = store.store_id
            INNER JOIN users
            ON instruments.user_id = users.user_id
            WHERE users.email=?;
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
