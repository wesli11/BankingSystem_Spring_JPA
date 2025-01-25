package com.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@EnableTransactionManagement
@ComponentScan(basePackages = {"com.bank.repository","com.bank.service"})
@Configuration
public class SpringConfig {

	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource data=new DriverManagerDataSource();
		data.setDriverClassName("com.mysql.cj.jdbc.Driver");
		data.setUrl("jdbc:mysql://localhost:3306/bancabd?serverTimezone=UTC&useSSL=false");
		data.setUsername("root");
		data.setPassword("Muj@j@0101");
		return data;
	}
	
	//adaptador de Hibernate
		@Bean
		public HibernateJpaVendorAdapter adapter() {
			HibernateJpaVendorAdapter adp=new HibernateJpaVendorAdapter();
			adp.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
			return adp;
		}
		//factoria EntityManager: Objeto para acceder a capa de persistencia con JPA
		@Bean
	    public EntityManagerFactory entityManagerFactory() {
	        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
	        factoryBean.setDataSource(dataSource()); // El datasource que definimos
	        factoryBean.setPackagesToScan("com.bank.model"); // El paquete donde están tus entidades
	        factoryBean.setJpaVendorAdapter(adapter()); // Usamos el adaptador definido antes
	        factoryBean.afterPropertiesSet(); // Asegura la inicialización completa
	        return factoryBean.getObject();
	    }
		
		 @Bean
		 public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		    return new JpaTransactionManager(entityManagerFactory);
		 }
	
}
