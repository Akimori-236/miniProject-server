package tfip.akimori.server.repositories;

public interface SQLQueries {

    // CREATE
    public final String SQL_INSERT_USER = """
            INSERT INTO users(givenname, familyname, email, password, role, isGoogleLogin) VALUES (?, ?, ?, ?, ?, ?)
            """;
    public final String SQL_INSERT_STORE = """
            INSERT INTO stores(store_name) VALUES(?)
            """; // must be executed with INSERT MANAGER
    public final String SQL_INSERT_MANAGER = """
            INSERT INTO managers(manager_id, store_id) VALUES((
                SELECT user_id
                FROM users
                WHERE email=?), ?)
            """; // email, store_id
    public final String SQL_INSERT_INSTRUMENT = """
            INSERT INTO instruments(instrument_type, brand, model, serial_number, store_id, outForRepair)
            VALUES(?, ?, ?, ?, ?, 0)
            """;

    // READ
    public final String SQL_GETUSERBYEMAIL = """
            SELECT * FROM users WHERE email=?
            """;
    public final String SQL_GETMANAGEDINSTRUMENTSBYEMAIL = """
            SELECT instrument_id, instrument_type, brand, model, serial_number, store_name, u2.givenname, u2.familyname, u2.email
            FROM instruments i
            INNER JOIN stores s
            ON i.store_id = s.store_id
            INNER JOIN managers m
            ON m.store_id = s.store_id
            INNER JOIN users u
            ON m.manager_id = u.user_id
            LEFT JOIN users u2
            ON i.user_id = u2.user_id
            WHERE u.email=?
            """;
    public final String SQL_GETBORROWEDBYEMAIL = """
            SELECT instrument_id, instrument_type, brand, model, serial_number, store_name, givenname, familyname, email
            FROM instruments i
            INNER JOIN stores s
            ON i.store_id = s.store_id
            INNER JOIN users u
            ON i.user_id = u.user_id
            WHERE u.email=?
            """;

    // UPDATE
    public final String SQL_LOANOUT_INSTRUMENT = """

            """;
    public final String SQL_RETURN_INSTRUMENT = """

            """;
    public final String SQL_ISSUE_CONSUMABLE = """

            """;
    public final String SQL_REPLENISH_CONSUMABLE = """

            """;
}
