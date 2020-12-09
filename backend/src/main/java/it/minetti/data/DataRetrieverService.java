package it.minetti.data;

import it.minetti.pcmdpc.CsvRow;
import it.minetti.pcmdpc.RemoteCsvExtractor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataRetrieverService {


    private final RemoteCsvExtractor remoteCsvExtractor;
    private final CsvDataMapper mapper;

    public DataRetrieverService(RemoteCsvExtractor remoteCsvExtractor, CsvDataMapper mapper) {
        this.remoteCsvExtractor = remoteCsvExtractor;
        this.mapper = mapper;
    }

    public DataModel retrieveNationalData() {
        List<CsvRow> csvRows = remoteCsvExtractor.retrieveLastNationalData();
        return mapper.map(csvRows);
    }

    public DataModel retrieveRegionalData(String regionCode) {
        List<CsvRow> csvRows = remoteCsvExtractor.retrieveLastRegionalData();
        List<CsvRow> filteredRows = csvRows.stream()
                .filter(r -> StringUtils.equals(r.getRegionCode(), regionCode))
                .collect(Collectors.toList());

        if (filteredRows.isEmpty()) {
            throw new RegionNotFoundException();
        }

        return mapper.map(filteredRows);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Region not Found")
    public static class RegionNotFoundException extends RuntimeException {
    }

}
