package tfip.akimori.server.repositories;

public interface SQLQueries {

    // CREATE
    public final String SQL_INSERTUSER = """
            INSERT INTO users(firstname, lastname, email, password, role) VALUES (?, ?, ?, ?, ?)
                """;
    public final String SQL_CREATENEWSTORE = """

            """;

    // READ
    public final String SQL_FINDBYEMAIL = """
            SELECT * FROM users WHERE email=?
                """;
    public final String SQL_GETINSTRUMENTSBYEMAIL = """
            SELECT instrument_id, section, brand, model, serial_number, store_name, firstname, lastname
            FROM instruments i
            INNER JOIN store s
            ON i.store_id = s.store_id
            INNER JOIN users u
            ON i.user_id = u.user_id
            WHERE u.email=?;
                    """;
    public final String SQL_GETSTOREBYEMAIL = """
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

    // UPDATE
    public final String SQL_LOANOUT = """

            """;
    public final String SQL_RETURN = """

            """;
}
