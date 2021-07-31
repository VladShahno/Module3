package ua.com.alevel.model.dto.operation;

import ua.com.alevel.model.dto.entity.CategoryDTO;

import java.util.List;

public class OperationRequest {

    private Long typeId;

    private Long accountId;

    private List<CategoryDTO> categories;

    private Double amount;

    public OperationRequest(Long type_id, Long account_id, List<CategoryDTO> categories, Double amount) {
        this.typeId = type_id;
        this.accountId = account_id;
        this.categories = categories;
        this.amount = amount;
    }

    public Long getTypeId() {
        return typeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public Double getAmount() {
        return amount;
    }
}
