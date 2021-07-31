package ua.com.alevel.model.dto.export;

import java.time.Instant;

public class ExportRequest {

    Long accountId;

    Instant startDate;

    Instant endDate;

    public ExportRequest(Long accountId, Instant startDate, Instant endDate) {
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }
}
