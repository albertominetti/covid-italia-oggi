package it.minetti.data;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataModel {
    private LocalDate startDate;
    private List<Integer> inIntensiveCare;
    private List<Integer> newDeceased;
    private List<Integer> newPositives;
    private List<Integer> newTests;

    public DataModel(LocalDate fromDate, int size) {
        this.startDate = fromDate;
        this.inIntensiveCare = new ArrayList<>(size);
        this.newDeceased = new ArrayList<>(size);
        this.newPositives = new ArrayList<>(size);
        this.newTests = new ArrayList<>(size);
    }

    public LocalDate getLastDate() {
        return this.startDate.plusDays(inIntensiveCare.size() - 1);
    }
}
