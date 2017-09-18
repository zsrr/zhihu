package com.stephen.zhihu.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jsms.api.JSMSClient;
import cn.jsms.api.common.JSMSConfig;
import com.stephen.zhihu.Constants;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.hibernate.EhCacheRegionFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Configuration
@ComponentScan(basePackages =
        {"com.stephen.zhihu.dao",
                "com.stephen.zhihu.service",
                "com.stephen.zhihu.authorization"})
@EnableCaching
@EnableTransactionManagement
@EnableElasticsearchRepositories(basePackages = "com.stephen.zhihu.domain_elasticsearch", considerNestedRepositories = true)
public class RootConfig {

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setBlockWhenExhausted(true);
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        config.setMaxTotal(32);
        config.setMaxIdle(32);
        config.setMaxWaitMillis(300 * 1000);
        config.setMinEvictableIdleTimeMillis(15 * 60 * 1000);
        return new JedisPool(config, "127.0.0.1", 6379, 20 * 1000);
    }

    @Bean
    public JSMSClient jsmsClient() {
        ClassPathResource classPathResource = new ClassPathResource("jiguang.properties", this.getClass().getClassLoader());
        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadProperties(classPathResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new JSMSClient(properties.getProperty("masterSecret"), properties.getProperty("appKey"), null, JSMSConfig.getInstance());
    }

    @Bean
    public JPushClient jPushClient() {
        ClassPathResource classPathResource = new ClassPathResource("jiguang.properties", this.getClass().getClassLoader());
        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadProperties(classPathResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setApnsProduction(Constants.JIHUANG_CLIENT_IN_PRODUCTION);
        clientConfig.setConnectionTimeout(Constants.JIGUANG_CLIENT_CONNECTION_TIME_OUT);
        clientConfig.setMaxRetryTimes(Constants.JIGUANG_CLIENT_MAX_RETRY);

        return new JPushClient(properties.getProperty("masterSecret"),
                properties.getProperty("appKey"), null, clientConfig);
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean ehcache = new EhCacheManagerFactoryBean();
        ehcache.setConfigLocation(new ClassPathResource("ehcache.xml"));
        ehcache.setCacheManagerName("spring-cache");
        return ehcache;
    }

    @Bean
    public EhCacheCacheManager cacheManager(CacheManager cacheManager) {
        return new EhCacheCacheManager(cacheManager);
    }

    public static class HibernatePropertiesConfig {
        private final String dialect;
        private final String hbm2ddl_auto;

        public HibernatePropertiesConfig(String dialect, String hbm2ddl_auto) {
            this.dialect = dialect;
            this.hbm2ddl_auto = hbm2ddl_auto;
        }

        public String getDialect() {
            return dialect;
        }

        public String hbm2ddl_auto() {
            return hbm2ddl_auto;
        }
    }

    @Bean
    @Profile("pro")
    public DataSource mysqlDataSource() {
        DataSource dataSource;
        JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = jndi.lookup("java:comp/env/jdbc/zhihu", DataSource.class);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    @Bean
    @Profile("dev")
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2).
                setName("zhihu").
                build();
    }

    @Bean
    LocalSessionFactoryBean factoryBean(DataSource dataSource, HibernatePropertiesConfig propertiesConfig) {
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
        sfb.setDataSource(dataSource);
        sfb.setAnnotatedPackages("com.stephen.zhihu.domain_jpa");
        sfb.setPackagesToScan("com.stephen.zhihu.domain_jpa");
        Properties properties = new Properties();
        properties.put("hibernate.dialect", propertiesConfig.getDialect());
        properties.put("hibernate.hbm2ddl.auto", propertiesConfig.hbm2ddl_auto());
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.cache.use_second_level_cache", "true");
        properties.put("hibernate.cache.use_query_cache", "true");
        properties.put("hibernate.cache.region.factory_class", EhCacheRegionFactory.class.getName());
        properties.put("net.sf.ehcache.configurationResourceName", "ehcache.xml");
        properties.put("hibernate.cache.use_structured_entries", "false");
        properties.put("hibernate.generate_statistics", "true");
        sfb.setHibernateProperties(properties);
        return sfb;
    }

    @Bean
    @Profile("pro")
    public HibernatePropertiesConfig productionDialect() {
        return new HibernatePropertiesConfig("org.hibernate.dialect.MySQL57Dialect", "update");
    }

    @Bean
    @Profile("dev")
    public HibernatePropertiesConfig testDialect() {
        return new HibernatePropertiesConfig("org.hibernate.dialect.H2Dialect", "create");
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    // elasticsearch configuration
    // 远程连接集群
    @Bean(destroyMethod = "close")
    @Profile("pro")
    public Client transportClient() {
        TransportClient client = null;
        try {
            client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Bean(destroyMethod = "close")
    @Profile("dev")
    public Client nodeClient() {
        Settings settings = Settings.settingsBuilder().
                put("http.enabled", false).
                put("path.home", "/Users/zhangshirui/elasticsearch-2.4.0").
                put("path.data", "/Users/zhangshirui/zhihu-search-data").
                build();
        Node node = nodeBuilder().settings(settings).local(true).node();
        return node.client();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(Client client) {
        return new ElasticsearchTemplate(client);
    }
}
