package ua.com.alevel.model.dto.export;

import ua.com.alevel.model.dto.entity.OperationDTO;

import java.util.List;

public interface Exportable {

    public Double getIncomeAmountInPeriod();

    public Double getBalanceInPeriod();

    public List<OperationDTO> getOperation();

}
