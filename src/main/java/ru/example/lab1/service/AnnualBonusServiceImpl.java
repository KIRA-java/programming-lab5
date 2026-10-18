package ru.example.lab1.service;

import org.springframework.stereotype.Service;
import ru.example.lab1.model.Positions;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    @Override
    public double calculate(
            Positions position,
            double salary,
            double bonus,
            int workDays
    ) {
        return calculate(
                position, salary, bonus, workDays,
                Year.now().getValue()
        );
    }

    @Override
    public double calculate(
            Positions position,
            double salary,
            double bonus,
            int workDays,
            int year
    ) {
        if (position == null) {
            throw new IllegalArgumentException(
                    "Должность должна быть указана"
            );
        }

        if (!Double.isFinite(salary) || salary < 0) {
            throw new IllegalArgumentException(
                    "Зарплата должна быть конечным неотрицательным числом"
            );
        }

        if (!Double.isFinite(bonus) || bonus < 0) {
            throw new IllegalArgumentException(
                    "Множитель премии должен быть конечным неотрицательным числом"
            );
        }

        int daysInYear = Year.of(year).length();

        if (workDays < 1 || workDays > daysInYear) {
            throw new IllegalArgumentException(
                    "Количество отработанных дней должно быть от 1 до "
                            + daysInYear
            );
        }

        // Формула годовой премии из методички.
        return salary * bonus * daysInYear
                * position.getPositionCoefficient() / workDays;
    }

    @Override
    public double calculateQuarterly(
            Positions position,
            double salary,
            double bonus,
            int workDays
    ) {
        return calculateQuarterly(
                position, salary, bonus, workDays,
                Year.now().getValue()
        );
    }

    @Override
    public double calculateQuarterly(
            Positions position,
            double salary,
            double bonus,
            int workDays,
            int year
    ) {
        if (position == null || !position.isManager()) {
            throw new IllegalArgumentException(
                    "Квартальная премия доступна только руководителям"
            );
        }

        // Принятое правило: квартальная премия — четверть годовой.
        return calculate(
                position, salary, bonus, workDays, year
        ) / 4.0;
    }
}