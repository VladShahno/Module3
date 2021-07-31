package ua.com.alevel.service.impl;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.alevel.dao.jpaImpl.*;
import ua.com.alevel.model.dto.authorization.AuthorizationRequest;
import ua.com.alevel.model.dto.authorization.AuthorizationResult;
import ua.com.alevel.model.dto.entity.CategoryDTO;
import ua.com.alevel.model.dto.entity.TypeDTO;
import ua.com.alevel.model.dto.operation.OperationRequest;
import ua.com.alevel.model.dto.operation.OperationResult;
import ua.com.alevel.model.entity.Account;
import ua.com.alevel.model.entity.Category;
import ua.com.alevel.model.entity.Operation;
import ua.com.alevel.model.entity.Type;
import ua.com.alevel.service.Authorizable;

import java.util.*;

public class JPAEntityService implements Authorizable {

    CategoryDao categoryDAO;
    TypeDao typeDAO;
    UserDao userDAO;
    AccountDao accountDAO;
    OperationDao operationDAO;
    Session session;

    private static final Logger log = LoggerFactory.getLogger(JPAEntityService.class);

    public JPAEntityService(Session session) {
        this.session = session;
        this.categoryDAO = new CategoryDao(session);
        this.typeDAO = new TypeDao(session);
        this.userDAO = new UserDao(session);
        this.accountDAO = new AccountDao(session);
        this.operationDAO = new OperationDao(session);
    }


    public List<CategoryDTO> getCategoryByTypeId(Long id) throws Exception {
        log.info("Starting search for categories");
        try {
            session.beginTransaction();
            List<Category> categories = categoryDAO.findByTypeId(id);
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            for (Category category : categories) {
                categoryDTOS.add(new CategoryDTO(category.getId(), category.getName()));
            }
            session.getTransaction().commit();
            log.info("Returning categories");
            return categoryDTOS;
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("GetCategory failed");
            throw new Exception(e.getMessage());
        }
    }

    public List<TypeDTO> getAllTypes() throws Exception {
        log.info("Starting search for all types of operation");
        try {
            session.beginTransaction();
            List<Type> types = typeDAO.readAll();
            List<TypeDTO> typeDTOS = new ArrayList<>();
            for (Type type : types) {
                typeDTOS.add(new TypeDTO(type.getId(), type.getName()));
            }
            session.getTransaction().commit();
            log.info("Returning types");
            return typeDTOS;
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("GetAllTypes failed");
            throw new Exception(e.getMessage());
        }
    }

    public AuthorizationResult getAuthorize(AuthorizationRequest request) throws Exception {
        log.info("Starting authorization");
        try {
            session.beginTransaction();
            if (accountDAO.checkAuthorization(request)) {
                Account account = accountDAO.findByUserName(request.getUserName()).get();
                session.getTransaction().commit();
                log.info("Authorization is done success");
                return new AuthorizationResult(true, account.getId(), account.getAmount());
            } else
                log.info("Authorization is done failed");
            return new AuthorizationResult(false, null, null);
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("GetAuthorize failed");
            throw new Exception(e.getMessage());
        }
    }

    public OperationResult createOperation(OperationRequest request) throws Exception {
        log.info("Starting create operation");
        try {
            session.beginTransaction();
            List<Category> categoriesOfType = categoryDAO.findByTypeId(request.getTypeId());
            Operation operation = new Operation();
            Set<Category> categoryList = new HashSet<>();
            String message = null;
            boolean status = false;
            Double currAmount;
            boolean checkCategories = true;

            if (accountDAO.checkIfExistById(request.getAccountId())) {
                Account account = accountDAO.findById(request.getAccountId()).get();

                if (typeDAO.checkIfExistById(request.getTypeId())) {

                    if (request.getAmount() != 0) {
                        Optional<Category> category;
                        for (CategoryDTO categoryDTO : request.getCategories()) {
                            category = categoryDAO.findById(categoryDTO.getId());
                            if (categoriesOfType.contains(category.get())) {
                                categoryList.add(category.get());
                            } else {
                                checkCategories = false;
                                message = "Invalid category for selected type of operation";
                                break;
                            }
                        }
                        if (checkCategories) {
                            Type type = typeDAO.findById(request.getTypeId()).get();
                            operation.setAccount(account);
                            operation.setType(type);
                            operation.setCategories(categoryList);
                            operation.setAmount(request.getAmount());
                            operationDAO.create(operation);
                            status = true;
                            message = "Operation is done";
                            if (type.getName().equals("income"))
                                account.setAmount(account.getAmount() + request.getAmount());
                            else if (type.getName().equals("expense"))
                                account.setAmount(account.getAmount() - request.getAmount());
                            accountDAO.update(account);
                        }

                    } else message = "Amount can`t be 0";
                } else message = "Selected type doesn`t exist";

                currAmount = account.getAmount();

            } else {
                message = "Account doesn`t exist";
                currAmount = null;
            }
            session.getTransaction().commit();
            log.info("Returning result of creation");
            return new OperationResult(status, currAmount, message);

        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("Operation creation failed");
            throw new Exception(e.getMessage());
        }
    }


}
