package stenden.spring.configuration;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement // Required for Hibernate
@EnableJpaRepositories("stenden.spring.data")
public class DatabaseConfig {

  /**
   * The {@link DataSource} representing the database connection.
   * In our case we're creating an in-memory database using H2,
   * so the setup is simple.
   *
   * @return The connection to the database
   */
  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/java-juke");
    dataSource.setUsername("root");
    dataSource.setPassword("");

    return dataSource;
  }

  /**
   * JDBC by itself is tough to use, so we wrap it in the {@link JdbcTemplate} from Spring,
   * which handles a lot of the boilerplate for us.
   * Pure JDBC has its uses though. While it gives a lot of boilerplate,
   * it also gives a lot of control, which can be useful for certain applications.
   * Think of having to make very complex queries for very specific situations.
   *
   * @param dataSource
   * @return
   */
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
    LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
    sfb.setDataSource(dataSource);
    sfb.setPackagesToScan("stenden.spring.data.model");
    Properties props = new Properties();
    props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
    sfb.setHibernateProperties(props);
    return sfb;
  }

  @Bean
  public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory);
    return transactionManager;
  }

  /**
   * Whhn just using JPA, you could also use this transaction manager.
   *
   * @return
   */
//  @Bean
//  public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
//    JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//    jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
//    return jpaTransactionManager;
//  }
  @Bean
  public BeanPostProcessor persistenceTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setDatabase(Database.H2);
    return adapter;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
    entityManagerFactoryBean.setPackagesToScan("stenden.spring.data.model");
    return entityManagerFactoryBean;
  }
}
