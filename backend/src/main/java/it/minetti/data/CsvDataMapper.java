package it.minetti.data;

import it.minetti.pcmdpc.RemoteCsvExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CsvDataMapper {

    public DataModel map(List<RemoteCsvExtractor.CsvRow> rows) {
        log.info("Mapping {} data rows...", rows.size());
        RemoteCsvExtractor.CsvRow first = rows.get(0);

        DataModel dataModel = new DataModel(first.getDate(), rows.size());

        RemoteCsvExtractor.CsvRow previous = new RemoteCsvExtractor.CsvRow();
        for (RemoteCsvExtractor.CsvRow row : rows) {
            dataModel.getNewPositives().add(row.getNewPositives());
            dataModel.getNewDeceased().add(row.getTotalDeceased() - previous.getTotalDeceased());
            dataModel.getInIntensiveCare().add(row.getInIntensiveCare());
            dataModel.getNewTests().add(row.getTotalTests() - previous.getTotalTests());
            previous = row;
        }

        log.info("Mapped {} data rows.", rows.size());
        return dataModel;
    }
}
