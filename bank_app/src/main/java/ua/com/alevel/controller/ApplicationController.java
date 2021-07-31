package ua.com.alevel.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.alevel.Main;
import ua.com.alevel.model.dto.authorization.AuthorizationRequest;
import ua.com.alevel.model.dto.authorization.AuthorizationResult;
import ua.com.alevel.model.dto.entity.CategoryDTO;
import ua.com.alevel.model.dto.entity.TypeDTO;
import ua.com.alevel.model.dto.export.ExportRequest;
import ua.com.alevel.model.dto.export.ExportResult;
import ua.com.alevel.model.dto.operation.OperationRequest;
import ua.com.alevel.model.dto.operation.OperationResult;
import ua.com.alevel.service.impl.JDBCEntityService;
import ua.com.alevel.service.impl.JPAEntityService;
import ua.com.alevel.util.CSVUtil;
import ua.com.alevel.util.HibernateUtil;
import ua.com.alevel.util.PropertiesLoaderUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ApplicationController {

    private static final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);


    public static void runUserInterface(String[] strings) {

        String check, password, userName;
        boolean work = true;

        while (work) {
            try {
                System.out.println("Choose your option from 1 to 3 and press Enter to select: " +
                        "\n 1 - create operation" +
                        "\n 2 - export account statements in csv format " +
                        "\n 3 - exit");
                check = in.readLine();
                log.info("User selected mode #" + check);
                Long userId = Long.parseLong(strings[0]);
                userName = strings[1];
                password = strings[2];
                log.info("Entered info: " + strings);

                switch (check) {
                    case "1":
                        log.info("Starting first mode");
                        boolean sessionOpen = true;

                        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory(password);
                             Session session = sessionFactory.openSession()) {
                            JPAEntityService entityService = new JPAEntityService(session);
                            AuthorizationResult result = entityService.getAuthorize(new AuthorizationRequest(userId, userName));
                            log.info("Authorization status " + result.getStatus());

                            if (!result.getStatus()) {
                                throw new Exception("Wrong user id or user name"); }

                            Long accountId = result.getAccountId();
                            var types = entityService.getAllTypes();

                            while (sessionOpen) {
                                System.out.println("Select id of type of operation: ");

                                for (TypeDTO type : types) {
                                    System.out.println(type.getId() + " - " + type.getName()); }
                                Long typeId = Long.parseLong(in.readLine());
                                log.info("Selected type id: " + typeId);

                                var categories = entityService.getCategoryByTypeId(typeId);
                                System.out.println("Enter id of categories in line:  ");

                                for (CategoryDTO category : categories) {
                                    System.out.println(category.getId() + " - " + category.getName()); }
                                String catIds = in.readLine();
                                log.info("Selected categories id: " + catIds);
                                List<CategoryDTO> requestCategories = new ArrayList<>();

                                for (CategoryDTO category : categories)
                                    if (catIds.contains(String.valueOf(category.getId()))) {
                                        requestCategories.add(category);
                                    }
                                System.out.println("Enter amount of money: ");
                                Double amount = Double.valueOf(in.readLine());
                                log.info("Entered amount: " + amount);
                                OperationResult operationResult = entityService.
                                        createOperation(new OperationRequest(typeId, accountId, requestCategories, amount));
                                log.info("Operation creation status: " + operationResult.getStatus());

                                if (!operationResult.getStatus())
                                    throw new Exception(operationResult.getMessage());
                                else
                                    System.out.println(operationResult.getMessage() + "\nCurrent amount: "
                                            + operationResult.getAmount());
                                System.out.println("Continue add operation? Y/N");

                                if (!in.readLine().equals("Y")) {
                                    sessionOpen = false;
                                    log.info("Exit from first mode");
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            throw new Exception(e.getMessage());
                        }
                        break;
                    case "2":
                        log.info("Starting second mode");
                        boolean connectionOpen = true;
                        Properties props = PropertiesLoaderUtil.loadProperties(password);
                        String url = props.getProperty("url");

                        try (Connection connection = DriverManager.getConnection(url, props)) {
                            JDBCEntityService jdbcEntityService = new JDBCEntityService(connection);
                            AuthorizationResult result = jdbcEntityService.getAuthorize(new AuthorizationRequest(userId, userName));
                            log.info("Authorization status " + result.getStatus());

                            if (!result.getStatus())
                                throw new Exception("Wrong user id or user name");
                            Long accountId = result.getAccountId();

                            while (connectionOpen) {
                                System.out.println("Enter amount of days before today: ");
                                int days = Integer.parseInt(in.readLine());
                                log.info("Entered amount of days: " + days);
                                Instant start = Instant.now().minus(days, ChronoUnit.DAYS);
                                ExportResult exportResult = jdbcEntityService.exportInfoAboutOperation(new ExportRequest(accountId, start, Instant.now()));

                                if (exportResult.getOperation() == null)
                                    throw new Exception("No data to export");
                                CSVUtil.outputInFile("data.csv", exportResult);
                                System.out.println("Data is exported");
                                System.out.println("Continue export information? Y/N");

                                if (!in.readLine().equals("Y"))
                                    log.info("Exit from second mode");
                                connectionOpen = false;
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            throw new Exception(e.getMessage());
                        }
                        break;
                    default:
                        break;
                }
                System.out.println("Continue program? Y/N");
                if (!in.readLine().equals("Y"))
                    work = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
