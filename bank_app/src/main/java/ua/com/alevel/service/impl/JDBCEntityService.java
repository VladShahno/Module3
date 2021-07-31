package ua.com.alevel.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.alevel.model.dto.authorization.AuthorizationRequest;
import ua.com.alevel.model.dto.authorization.AuthorizationResult;
import ua.com.alevel.model.dto.entity.CategoryDTO;
import ua.com.alevel.model.dto.entity.OperationDTO;
import ua.com.alevel.model.dto.entity.TypeDTO;
import ua.com.alevel.model.dto.export.ExportRequest;
import ua.com.alevel.model.dto.export.ExportResult;
import ua.com.alevel.service.Authorizable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCEntityService implements Authorizable {

    private Connection connection;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long id;

    private static final Logger log = LoggerFactory.getLogger(JDBCEntityService.class);

    public JDBCEntityService(Connection connection) {
        this.connection = connection;
    }

    public ExportResult exportInfoAboutOperation(ExportRequest request) throws Exception {
        this.startDate = Timestamp.from(request.getStartDate());
        this.endDate = Timestamp.from(request.getEndDate());
        this.id = request.getAccountId();
        return new ExportResult(getOperationsInfo(), getIncomeAmountOfAccountInPeriod(), getBalanceInPeriod());
    }

    private List<OperationDTO> getOperationsInfo() throws Exception {
        log.info("Start search operation info");
        try (PreparedStatement getValue = connection.prepareStatement(
                "SELECT o.id, o.amount, o.date, o.type_id FROM operations o " +
                        "JOIN accounts c ON o.account_id = c.id WHERE (c.id = ?) and (o.date BETWEEN ? AND ?)")) {
            getValue.setLong(1, id);
            getValue.setTimestamp(2, startDate);
            getValue.setTimestamp(3, endDate);
            ResultSet resultSet = getValue.executeQuery();

            List<OperationDTO> data = new ArrayList<>();
            while (resultSet.next()) {
                Long operationId = resultSet.getLong("id");
                data.add(new OperationDTO(operationId,
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("date").toInstant(),
                        getCategoriesByOperationId(operationId),
                        getTypeNameById(resultSet.getLong("type_id"))));
            }
            log.info("Returned data size " + data.size());
            return data;
        } catch (Exception e) {
            log.error("Failed search for operation");
            throw new Exception(e.getMessage());
        }
    }

    private Double getIncomeAmountOfAccountInPeriod() throws Exception {
        log.info("Start search for income amount in period " + startDate + " - " + endDate);
        try (PreparedStatement getValue = connection.prepareStatement(
                "SELECT sum(o.amount) AMOUNT FROM operations o JOIN types t ON o.type_id = t.id " +
                        "WHERE (o.date BETWEEN ? AND ?) AND t.name like 'income' AND o.account_id = ?")) {
            getValue.setTimestamp(1, startDate);
            getValue.setTimestamp(2, endDate);
            getValue.setLong(3, id);
            ResultSet resultSet = getValue.executeQuery();
            if (resultSet.next()) {
                log.info("Return result");
                return resultSet.getDouble("AMOUNT");
            }
            log.info("Empty result");
            return null;
        } catch (Exception e) {
            log.error("Failed search for income amount");
            throw new Exception(e.getMessage());
        }
    }

    private Double getBalanceInPeriod() throws Exception {
        log.info("Start search for sal`do in period " + startDate + " - " + endDate);
        try (PreparedStatement getValue = connection.prepareStatement(
                "SELECT sum(o.amount) - (" +
                        "SELECT sum(op.amount)" +
                        "FROM operations op JOIN types t ON op.type_id = t.id " +
                        "WHERE op.date BETWEEN ? AND ? " +
                        "AND op.account_id = ? AND t.name like 'expense' " +
                        ") BALANCE " +
                        "FROM operations o JOIN types tp ON o.type_id = tp.id" +
                        " WHERE o.date BETWEEN ? AND ?" +
                        " AND o.account_id = ? AND tp.name like 'income'")) {
            getValue.setTimestamp(1, startDate);
            getValue.setTimestamp(2, endDate);
            getValue.setLong(3, id);
            getValue.setTimestamp(4, startDate);
            getValue.setTimestamp(5, endDate);
            getValue.setLong(6, id);
            ResultSet resultSet = getValue.executeQuery();
            if (resultSet.next()) {
                log.info("Return result");
                return resultSet.getDouble("BALANCE");
            }
            log.info("Empty result");
            return null;
        } catch (Exception e) {
            log.error("Failed search for sal`do");
            throw new Exception(e.getMessage());
        }
    }

    private TypeDTO getTypeNameById(Long typeId) throws Exception {
        log.info("Getting type by id");
        try (PreparedStatement getValue = connection.prepareStatement(
                "SELECT * FROM types WHERE id = ?")) {
            getValue.setLong(1, typeId);
            ResultSet resultSet = getValue.executeQuery();
            if (resultSet.next()) {
                log.info("Return result");
                return new TypeDTO(resultSet.getLong("id"), resultSet.getString("name"));
            }
            log.info("Empty result");
            return null;
        } catch (SQLException throwables) {
            log.error("Failed search for type by id");
            throw new Exception(throwables.getMessage());
        }
    }

    private List<CategoryDTO> getCategoriesByOperationId(Long operationId) throws Exception {
        log.info("Start search for categories by operation id");
        try (PreparedStatement getValue = connection.prepareStatement(
                "SELECT * FROM categories c JOIN operations_categories op ON op.category_id = c.id WHERE op.operation_id = ?")) {
            getValue.setLong(1, operationId);
            ResultSet resultSet = getValue.executeQuery();
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            while (resultSet.next()) {
                categoryDTOS.add(new CategoryDTO(resultSet.getLong("id"), resultSet.getString("name")));
            }
            log.info("Size of returned data: " + categoryDTOS.size());
            return categoryDTOS;
        } catch (SQLException throwables) {
            log.error("Failed search for categories by operation id");
            throw new Exception(throwables.getMessage());
        }
    }

    @Override
    public AuthorizationResult getAuthorize(AuthorizationRequest request) throws Exception {
        log.info("Authorizing");
        try (PreparedStatement getValue = connection.prepareStatement(
                "SELECT (count(c.id) > 0) EXIST, c.id Id, c.amount amount " +
                        "FROM accounts c WHERE c.userName LIKE ? AND c.user_id = ?" +
                        " GROUP BY c.id, c.amount")) {
            getValue.setString(1, request.getUserName());
            ;
            getValue.setLong(2, request.getUserId());
            ResultSet resultSet = getValue.executeQuery();
            resultSet.next();
            log.info("Return result");
            return new AuthorizationResult(resultSet.getBoolean("EXIST"), resultSet.getLong("Id"), resultSet.getDouble("amount"));
        } catch (SQLException throwables) {
            log.error("Failed authorization");
            throw new Exception(throwables.getMessage());
        }
    }


}
