package ua.com.alevel.util;

import com.opencsv.CSVWriter;
import ua.com.alevel.model.dto.entity.CategoryDTO;
import ua.com.alevel.model.dto.entity.OperationDTO;
import ua.com.alevel.model.dto.export.Exportable;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CSVUtil {

    public static void outputInFile(String fileName, Exportable information) throws Exception {

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            if (!information.getOperation().isEmpty()) {
                writer.writeNext(getFieldName(OperationDTO.class));
                writer.writeAll(convertListOfOperationDTO(information.getOperation()));
                writer.writeNext(new String[]{"Income amount: ", String.valueOf(information.getIncomeAmountInPeriod())});
                writer.writeNext(new String[]{"Sal`do: ", String.valueOf(information.getBalanceInPeriod())});
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private static List<String[]> convertListOfOperationDTO(List<OperationDTO> operationDTOS) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC));
        List<String[]> data = new ArrayList<>();
        for (OperationDTO op : operationDTOS) {
            String categories = "";
            for (CategoryDTO category : op.getCategories())
                categories = categories.concat(category.getName() + " ");
            data.add(new String[]{
                    String.valueOf(op.getId()),
                    String.valueOf(op.getAmount()),
                    formatter.format(op.getData()),
                    categories,
                    op.getType().getName()
            });
        }
        return data;
    }

    private static <T> String[] getFieldName(Class<T> classOfInstance) throws Exception {
        String[] names;
        try {
            Field[] fields = classOfInstance.getDeclaredFields();
            names = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                names[i] = fields[i].getName();
            }
            return names;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
