package org.example.star.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class RecommendationsDataSourceConfiguration {
    @Bean(name = "recommendationsH2DataSource")
    public DataSource recommendationsH2DataSource(@Value("${application.recommendations-db.url}") String recommendationsUrl) {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(recommendationsUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean(name = "recommendationsH2JdbcTemplate")
    public JdbcTemplate recommendationsH2JdbcTemplate(
            @Qualifier("recommendationsH2DataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = "recommendationsPostgresDataSource")
    public DataSource recommendationsPostGreeDataSource(@Value("${application.dynamic-recommendations-db.url}") String Url,
                                                        @Value("${application.dynamic-recommendations-db.username}") String username,
                                                        @Value("${application.dynamic-recommendations-db.password}") String password) {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(Url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

    @Bean(name = "recommendationsPostgresJdbcTemplate")
    public JdbcTemplate recommendationsPostgresJdbcTemplate(
            @Qualifier("recommendationsPostgresDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
