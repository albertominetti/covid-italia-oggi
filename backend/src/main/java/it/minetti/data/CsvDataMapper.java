package it.minetti.data;

import it.minetti.pcmdpc.CsvRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CsvDataMapper {

    public DataModel map(List<CsvRow> rows) {
        log.debug("Mapping {} data rows...", rows.size());
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Cannot map empty rows");
        }

        CsvRow first = rows.get(0);

        DataModel dataModel = new DataModel(first.getDate(), rows.size());

        CsvRow previous = new CsvRow();
        for (CsvRow row : rows) {
            dataModel.getNewPositives().add(row.getNewPositives());
            dataModel.getNewDeceased().add(row.getTotalDeceased() - previous.getTotalDeceased());
            dataModel.getInIntensiveCare().add(row.getInIntensiveCare());
            dataModel.getNewTests().add(row.getTotalTests() - previous.getTotalTests());
            previous = row;
        }

        log.debug("Mapped {} data rows.", rows.size());
        return dataModel;
    }
}
