package it.minetti.data;

import it.minetti.pcmdpc.RemoteCsvExtractor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataRetrieverService {

    private static final String NATIONAL_DATA = "national-data";

    private final RemoteCsvExtractor remoteCsvExtractor;
    private final CsvDataMapper mapper;

    public DataRetrieverService(RemoteCsvExtractor remoteCsvExtractor, CsvDataMapper mapper) {
        this.remoteCsvExtractor = remoteCsvExtractor;
        this.mapper = mapper;
    }

    @Cacheable(value = NATIONAL_DATA)
    public DataModel retrieveNationalData() {
        List<RemoteCsvExtractor.CsvRow> csvRows = remoteCsvExtractor.retrieveLastData();
        return mapper.map(csvRows);
    }

    @CacheEvict(value = NATIONAL_DATA, allEntries = true)
    public void evictAllData() {
    }
}
