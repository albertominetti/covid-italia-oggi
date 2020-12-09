package it.minetti.scheduled;

import it.minetti.images.LocalGraphsService;
import it.minetti.images.r.CovidGraphsGenerator;
import it.minetti.pcmdpc.CsvRow;
import it.minetti.pcmdpc.RemoteCsvExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class DataSchedulerService {

    private final RemoteCsvExtractor remoteCsvExtractor;
    private final LocalGraphsService localGraphsService;
    private final CovidGraphsGenerator covidGraphsGenerator;


    public DataSchedulerService(RemoteCsvExtractor remoteCsvExtractor,
                                LocalGraphsService localGraphsService,
                                CovidGraphsGenerator covidGraphsGenerator) {
        this.remoteCsvExtractor = remoteCsvExtractor;
        this.localGraphsService = localGraphsService;
        this.covidGraphsGenerator = covidGraphsGenerator;
    }

    @Scheduled(cron = "0 55 17 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 00 18 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 10 18 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 15 19 * * ?", zone = "Europe/Rome")
    public void evictAndRetrieveDataIfNewData() {
        Optional<LocalDate> optionalLastDayOfData = remoteCsvExtractor.retrieveLastNationalData().stream()
                .map(CsvRow::getDate).max(LocalDate::compareTo);
        if (optionalLastDayOfData.isEmpty()) {
            log.warn("No data has ever been retrieved since now, seems it is the first time.");
            remoteCsvExtractor.retrieveLastNationalData();
            remoteCsvExtractor.retrieveLastRegionalData();
        } else {
            LocalDate lastDayOfData = optionalLastDayOfData.get();
            if (lastDayOfData.isEqual(LocalDate.now())) {
                // the data for the day are published within the day
                log.warn("No needs to retrieve new data graphs since they are updated to {}.", lastDayOfData);
                return;
            }

            LocalDate lastDayFromCsv = remoteCsvExtractor.retrieveLastDayInCsv();
            if (lastDayOfData.isBefore(lastDayFromCsv)) {
                log.info("There are new data, evict the old ones.");
                remoteCsvExtractor.evictAllData();
                remoteCsvExtractor.retrieveLastNationalData();
                remoteCsvExtractor.retrieveLastRegionalData();
            } else {
                log.warn("There are no data till now for the current day.");
            }

        }
    }

    @Scheduled(cron = "0 55 17 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 00 18 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 10 18 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 15 19 * * ?", zone = "Europe/Rome")
    public void generateImagesIfNewData() throws IOException {
        Optional<LocalDate> optionalLastDayOfCalculation = localGraphsService.retrieveLatestCalculatedDay();
        if (optionalLastDayOfCalculation.isEmpty()) {
            log.warn("No graph has ever been calculated since now, seems it is the first time.");
            covidGraphsGenerator.createLatestGraphs();
        } else {
            LocalDate lastDayOfCalculation = optionalLastDayOfCalculation.get();
            if (lastDayOfCalculation.isEqual(LocalDate.now())) {
                // the data for the day are published within the day
                log.warn("No needs to create the new graphs since they are updated to {}.", lastDayOfCalculation);
                return;
            }

            LocalDate lastDayFromCsv = remoteCsvExtractor.retrieveLastDayInCsv();
            if (lastDayOfCalculation.isBefore(lastDayFromCsv)) {
                log.info("There are new data for graphs calculation, running the extraction right now.");
                covidGraphsGenerator.createLatestGraphs();
            } else {
                log.warn("There are no data till now for the current day, wait for it and run manually or wait for the next run.");
            }

        }
    }

}
