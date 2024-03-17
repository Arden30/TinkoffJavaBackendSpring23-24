package edu.java.scrapper.integration_tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LiquibaseIntegrationTest extends IntegrationTest {
    private final String link = "https://github.com/Arden30/TinkoffJavaBackendSpring23-24";
    private final String date = "2024-03-09 18:00:00.0";
    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    @AfterEach
    void clear() throws SQLException {
        connection.prepareStatement("TRUNCATE TABLE chat CASCADE").executeUpdate();
        connection.prepareStatement("TRUNCATE TABLE link CASCADE").executeUpdate();
        connection.prepareStatement("TRUNCATE TABLE link_to_chat").executeUpdate();
    }

    @Test
    @DisplayName("Simple liquibase integration test")
    void test() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO link (url, created_at, updated_at) VALUES (?, ?, ?)");
        statement.setString(1, link);
        statement.setTimestamp(2, Timestamp.valueOf(date));
        statement.setTimestamp(3, Timestamp.valueOf(date));
        statement.executeUpdate();

        PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM link");
        ResultSet resultSet = selectStatement.executeQuery();

        while (resultSet.next()) {
            String url = resultSet.getString("url");
            Timestamp createdAt = resultSet.getTimestamp("created_at");

            assertThat(link).isEqualTo(url);
            assertThat(date).isEqualTo(createdAt.toString());
        }
    }
}
