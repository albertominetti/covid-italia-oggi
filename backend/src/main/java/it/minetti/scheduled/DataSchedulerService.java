package it.minetti.scheduled;

import it.minetti.data.DataRetrieverService;
import it.minetti.pcmdpc.RemoteCsvExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class DataSchedulerService {

    private final RemoteCsvExtractor remoteCsvExtractor;
    private final DataRetrieverService dataRetrieverService;

    public DataSchedulerService(RemoteCsvExtractor remoteCsvExtractor, DataRetrieverService dataRetrieverService) {
        this.remoteCsvExtractor = remoteCsvExtractor;
        this.dataRetrieverService = dataRetrieverService;
    }

    @Scheduled(cron = "0 55 17 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 00 18 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 10 18 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 15 19 * * ?", zone = "Europe/Rome")
    public void evictAndRetrieveIfNewData() { // TODO normalize with CovidGraphsGenerator.class
        LocalDate lastDayOfData = dataRetrieverService.retrieveNationalData().getLastDate();

        if (lastDayOfData.isEqual(LocalDate.now())) {
            // the data for the day are published within the day
            log.warn("No needs to retrieve new data graphs since they are updated to {}.", lastDayOfData);
            return;
        }

        LocalDate lastDayFromCsv = remoteCsvExtractor.retrieveLastDayInCsv();
        if (lastDayOfData.isBefore(lastDayFromCsv)) {
            log.info("There are new data, evict the old ones.");
            dataRetrieverService.evictAllData();
            dataRetrieverService.retrieveNationalData();
        } else {
            log.warn("There are no data till now for the current day.");
        }

    }
}
