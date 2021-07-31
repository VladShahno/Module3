package ua.com.alevel.model.dto.export;

import ua.com.alevel.model.dto.entity.OperationDTO;

import java.util.List;

public class ExportResult implements Exportable {

    private List<OperationDTO> operation;

    private Double incomeAmountInPeriod;

    private Double balanceInPeriod;

    public ExportResult(List<OperationDTO> operation, Double incomeAmountInPeriod, Double balanceInPeriod) {
        this.operation = operation;
        this.incomeAmountInPeriod = incomeAmountInPeriod;
        this.balanceInPeriod = balanceInPeriod;
    }

    public Double getIncomeAmountInPeriod() {
        return incomeAmountInPeriod;
    }

    public Double getBalanceInPeriod() {
        return balanceInPeriod;
    }

    public List<OperationDTO> getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "ExportResult{\n" +
                "operation=" + operation +
                ", \nincomeAmountInPeriod=" + incomeAmountInPeriod +
                ", \nbalanceInPeriod=" + balanceInPeriod +
                '}';
    }
}
