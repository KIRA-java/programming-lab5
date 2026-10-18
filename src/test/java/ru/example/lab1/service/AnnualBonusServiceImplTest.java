package ru.example.lab1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import ru.example.lab1.model.Positions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnnualBonusServiceImplTest {

    private final AnnualBonusService service =
            new AnnualBonusServiceImpl();

    private static final double DELTA = 0.000001;

    @Test
    void calculatesAnnualBonusForOrdinaryYear() {
        double result = service.calculate(
                Positions.HR, 100000, 2.0, 243, 2025
        );

        assertEquals(360493.8271604938, result, DELTA);
    }

    @Test
    void calculatesAnnualBonusForLeapYear() {
        double result = service.calculate(
                Positions.HR, 100000, 2.0, 243, 2024
        );

        assertEquals(361481.4814814815, result, DELTA);
    }

    @ParameterizedTest
    @CsvSource({
            "TL, 189800.0",
            "PO, 204400.0",
            "TPM, 219000.0",
            "CTO, 255500.0"
    })
    void calculatesQuarterlyBonusForManagers(
            Positions position,
            double expected
    ) {
        double result = service.calculateQuarterly(
                position, 100000, 2.0, 250, 2025
        );

        assertEquals(expected, result, DELTA);
    }

    @ParameterizedTest
    @EnumSource(
            value = Positions.class,
            names = {"DEV", "HR"}
    )
    void rejectsQuarterlyBonusForNonManagers(Positions position) {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.calculateQuarterly(
                        position, 100000, 2.0, 250, 2025
                )
        );
    }

    @Test
    void rejectsZeroWorkDays() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.calculate(
                        Positions.HR, 100000, 2.0, 0, 2025
                )
        );
    }

    @Test
    void rejects366WorkDaysForOrdinaryYear() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.calculate(
                        Positions.HR, 100000, 2.0, 366, 2025
                )
        );
    }
}