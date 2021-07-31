package ua.com.alevel.model.dto.operation;

public class OperationResult {

    private Boolean status;

    private Double amount;

    private String message;

    public OperationResult(Boolean status, Double amount, String message) {
        this.status = status;
        this.amount = amount;
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public Double getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "OperationResult{" +
                "status=" + status +
                ", amount=" + amount +
                ", message='" + message + '\'' +
                '}';
    }
}
