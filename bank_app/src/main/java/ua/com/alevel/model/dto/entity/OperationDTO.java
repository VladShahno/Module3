package ua.com.alevel.model.dto.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationDTO {

    private Long id;

    private Double amount;

    private Instant date;

    private List<CategoryDTO> categories;

    private TypeDTO type;

    public OperationDTO(Long id, Double amount, Instant data, TypeDTO type) {
        this.id = id;
        this.amount = amount;
        this.date = data;
        this.categories = new ArrayList<>();
        this.type = type;
    }

    public OperationDTO(Long id, Double amount, Instant date, List<CategoryDTO> categories, TypeDTO type) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.categories = categories;
        this.type = type;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "OperationDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", categories=" + categories +
                ", type=" + type +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Instant getData() {
        return date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public TypeDTO getType() {
        return type;
    }
}
