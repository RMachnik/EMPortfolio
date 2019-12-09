package pl.rmachnik.domain.infrastructure;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConfig {
    private static final Logger LOG = LoggerFactory.getLogger(DbConfig.class);
    private final DataSource dataSource;

    public DbConfig(String dbPath) {
        this.dataSource = initDataSource(dbPath);
        migrate();
        printAllTables();
    }

    private DataSource initDataSource(String dbPath) {
        String jdbcUrl = String.format("jdbc:sqlite:%s", dbPath);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setPoolName("Sqlite");

        LOG.info("Going to initialize connection to db {}", jdbcUrl);
        return new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }


    public void migrate() {
        Flyway flyway = Flyway.configure()
        .dataSource(dataSource)
        .load();
        flyway.migrate();
    }

    public void printAllTables() {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("select distinct tbl_name from sqlite_master");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LOG.info("{}", resultSet.getString(1));
            }
        } catch (SQLException e) {
            LOG.error("unable to print tables", e);
        }
    }

    public static void main(String[] args) {
        new DbConfig("portfolio.db");
    }
}
