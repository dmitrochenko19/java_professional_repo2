package ru.otus.cachehw;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.mapper.*;

import javax.sql.DataSource;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private final DbExecutor dbExecutor = new DbExecutorImpl();
    private static final DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
    private final TransactionRunnerJdbc transactionRunner = new TransactionRunnerJdbc(dataSource);
    private static final Logger log = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        flywayMigrations(dataSource);
        HWCacheDemo cacheDemo = new HWCacheDemo();
        cacheDemo.demo();
        cacheDemo.demo2();
        cacheDemo.demo3();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        @SuppressWarnings("java:S1604")
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put("1", 1);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);
    }

    private void demo2() {
        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(
                dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); // реализация DataTemplate, универсальная

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        //измеряем время на этапе сохранения и получения клиента
        long startTime = System.currentTimeMillis();
        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        long endTime = System.currentTimeMillis();

        log.info("clientSecondSelected:{}", clientSecondSelected);
        log.info("Time for Client with cache: {}", endTime - startTime);
    }

    private void demo3() {
        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        //измеряем время на этапе сохранения и получения менеджера
        long startTime = System.currentTimeMillis();
        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager
                .getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        long endTime = System.currentTimeMillis();

        log.info("managerSecondSelected:{}", managerSecondSelected);
        log.info("Time for Manager with cache: {}", endTime - startTime);
    }


    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
