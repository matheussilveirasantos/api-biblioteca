package br.com.escola.biblioteca.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Quando o banco é vinculado no Render via "Add from Database",
 * o painel injeta DATABASE_URL (formato postgres://), não DB_URL (JDBC).
 */
@Configuration
@ConditionalOnExpression("'${DB_URL:}'.isBlank() and !'${DATABASE_URL:}'.isBlank()")
public class RenderDatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource(@Value("${DATABASE_URL}") String databaseUrl) {
        try {
            URI uri = new URI(databaseUrl.replace("postgresql://", "postgres://"));

            String[] userInfo = uri.getUserInfo().split(":", 2);
            String username = decode(userInfo[0]);
            String password = userInfo.length > 1 ? decode(userInfo[1]) : "";

            int port = uri.getPort() == -1 ? 5432 : uri.getPort();
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();
            if (uri.getQuery() != null) {
                jdbcUrl += "?" + uri.getQuery();
            }

            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setMaximumPoolSize(5);
            dataSource.setMinimumIdle(2);
            dataSource.setConnectionTimeout(30_000);
            dataSource.setIdleTimeout(600_000);
            dataSource.setMaxLifetime(1_800_000);
            return dataSource;
        } catch (Exception ex) {
            throw new IllegalStateException(
                "Não foi possível interpretar DATABASE_URL. " +
                "Defina DB_URL no formato jdbc:postgresql://host:5432/banco ou vincule o PostgreSQL no Render.",
                ex
            );
        }
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
