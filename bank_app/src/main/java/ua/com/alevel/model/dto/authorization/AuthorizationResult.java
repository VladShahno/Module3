package ua.com.alevel.model.dto.authorization;

public class AuthorizationResult {

    private Boolean status;

    private Long accountId;

    private Double amount;

    public AuthorizationResult(Boolean status, Long account_id, Double amount) {
        this.status = status;
        this.accountId = account_id;
        this.amount = amount;
    }

    public Boolean getStatus() {
        return status;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "AuthorizationResult{" +
                "status=" + status +
                ", account_id=" + accountId +
                ", amount=" + amount +
                '}';
    }
}
