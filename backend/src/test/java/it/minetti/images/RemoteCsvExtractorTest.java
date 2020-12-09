package it.minetti.images;

import it.minetti.config.PcmDpcProperties;
import it.minetti.pcmdpc.CsvLocalDateFormatter;
import it.minetti.pcmdpc.CsvRow;
import it.minetti.pcmdpc.RemoteCsvExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RemoteCsvExtractorTest {

    @Mock
    PcmDpcProperties properties;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    RemoteCsvExtractor remoteCsvExtractor;

    @Test
    void testLastDay() {
        //given
        given(properties.getNationalCsvUrl()).willReturn("");
        given(restTemplate.getForObject("", String.class)).willReturn(
                "data,stato,ricoverati_con_sintomi,terapia_intensiva,etc\n" +
                        "2020-11-23T17:00:00,ITA,34697,3810,38507\n" +
                        "2020-11-24T17:00:00,ITA,34577,3816,38393\n");

        // when
        LocalDate localDate = remoteCsvExtractor.retrieveLastDayInCsv();

        // then
        assertThat(localDate, is(LocalDate.parse("2020-11-24")));
    }


    @Test
    void testAllData() {
        //given
        given(properties.getNationalCsvUrl()).willReturn("");
        given(restTemplate.getForObject("", String.class)).willReturn(
                "data,stato,ricoverati_con_sintomi,terapia_intensiva,etc\n" +
                        "2020-11-23T17:00:00,ITA,34697,3810,38507\n" +
                        "2020-11-24T17:00:00,ITA,34577,3816,38393\n");

        // when
        List<CsvRow> rows = remoteCsvExtractor.retrieveLastNationalData();

        // then
        assertThat(rows, is(notNullValue()));
        assertThat(rows, hasSize(2));

        rows.forEach(r -> {
            assertThat(r, is(notNullValue()));
            assertThat(r.getDate(), is(notNullValue()));
            assertThat(r.getInIntensiveCare(), is(notNullValue()));
        });

        System.out.println(rows);
    }

    @Test
    void testConverter() {
        //given
        CsvLocalDateFormatter formatter = new CsvLocalDateFormatter("yyyy-MM-dd'T'HH:mm:ss");

        // when
        LocalDate date = formatter.execute("2020-11-24T17:00:00");

        // then
        assertThat(date, is(LocalDate.parse("2020-11-24")));
    }
}