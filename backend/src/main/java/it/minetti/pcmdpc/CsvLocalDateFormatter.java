package it.minetti.pcmdpc;

import com.univocity.parsers.conversions.Conversion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CsvLocalDateFormatter implements Conversion<String, LocalDate> {

    private final DateTimeFormatter formatter;

    public CsvLocalDateFormatter(String... args) {
        String pattern = "yyyy-MM-dd";
        if (args.length > 0) {
            pattern = args[0];
        }
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public LocalDate execute(String input) {
        return LocalDate.parse(input, formatter);
    }

    @Override
    public String revert(LocalDate input) {
        return formatter.format(input);
    }
}
