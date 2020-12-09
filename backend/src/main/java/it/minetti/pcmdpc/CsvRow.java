package it.minetti.pcmdpc;

import com.univocity.parsers.annotations.Convert;
import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CsvRow {

    @Parsed(field = "data")
    @Convert(conversionClass = CsvLocalDateFormatter.class, args = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDate date;

    @Parsed(field = "deceduti")
    private int totalDeceased;

    @Parsed(field = "terapia_intensiva")
    private int inIntensiveCare;

    @Parsed(field = "nuovi_positivi")
    private int newPositives;

    @Parsed(field = "tamponi")
    private int totalTests;

    @Parsed(field = "codice_regione")
    private String regionCode;

    @Parsed(field = "denominazione_regione")
    private String regionName;
}
