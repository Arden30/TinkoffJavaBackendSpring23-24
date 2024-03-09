package edu.java.scrapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LiquibaseIntegrationTest extends IntegrationTest {
    String link = "https://github.com/Arden30/TinkoffJavaBackendSpring23-24";
    String date = "2024-03-09 18:00:00.0";

    @Test
    @DisplayName("Simple liquibase integration test")
    void test() throws Exception {
        Connection connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());

        PreparedStatement statement = connection.prepareStatement("INSERT INTO link (url, created_at) VALUES (?, ?)");
        statement.setString(1, link);
        statement.setTimestamp(2, Timestamp.valueOf(date));
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
