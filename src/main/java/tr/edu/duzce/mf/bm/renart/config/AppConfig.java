package tr.edu.duzce.mf.bm.renart.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;
import java.util.Properties;

import static org.hibernate.cfg.C3p0Settings.*;
import static org.hibernate.cfg.JdbcSettings.*;
import static org.hibernate.cfg.MappingSettings.DEFAULT_SCHEMA;
import static org.hibernate.cfg.SchemaToolingSettings.HBM2DDL_AUTO;

@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:hibernate.properties", encoding = "UTF-8")
@ComponentScans({
        @ComponentScan("tr.edu.duzce.mf.bm.renart.controller"),
        @ComponentScan("tr.edu.duzce.mf.bm.renart.service"),
        @ComponentScan("tr.edu.duzce.mf.bm.renart.dao")
})
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        Properties props = new Properties();

        try {
            props.put(DRIVER, env.getProperty("mysql.driver"));
            props.put(URL, env.getProperty("mysql.url"));
            props.put(USER, env.getProperty("mysql.user"));
            props.put(PASS, env.getProperty("mysql.password"));
            props.put(SHOW_SQL, env.getProperty("hibernate.show_sql"));
            props.put(HBM2DDL_AUTO, env.getProperty("hibernate.hbm2ddl.auto"));
            props.put(DIALECT, env.getProperty("hibernate.dialect"));
            props.put(DEFAULT_SCHEMA, env.getProperty("hibernate.default_schema"));
            props.put(C3P0_MIN_SIZE, env.getProperty("hibernate.c3p0.min_size"));
            props.put(C3P0_MAX_SIZE, env.getProperty("hibernate.c3p0.max_size"));
            props.put(C3P0_ACQUIRE_INCREMENT, env.getProperty("hibernate.c3p0.acquire_increment"));
            props.put(C3P0_TIMEOUT, env.getProperty("hibernate.c3p0.timeout"));
            props.put(C3P0_MAX_STATEMENTS, env.getProperty("hibernate.c3p0.max_statements"));
            props.put(C3P0_CONFIG_PREFIX + ".initialPoolSize", env.getProperty("hibernate.c3p0.initialPoolSize"));

            sessionFactory.setHibernateProperties(props);

        } catch (Exception e) {
        }

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }


    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages"); // messages.properties dosyalarını kullanacak
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("tr", "TR")); // Türkçe varsayılan
        resolver.setCookieName("lang");
        resolver.setCookieMaxAge(3600);
        return resolver;
    }


}
