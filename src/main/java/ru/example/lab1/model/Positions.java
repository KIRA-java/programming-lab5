package ru.example.lab1.model;

public enum Positions {

    DEV(2.2, false),
    HR(1.2, false),
    TL(2.6, true),
    PO(2.8, true),
    TPM(3.0, true),
    CTO(3.5, true);

    // Коэффициент премии для должности
    private final double positionCoefficient;

    // Является ли сотрудник руководителем
    private final boolean isManager;

    Positions(double positionCoefficient, boolean isManager) {
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
    }

    public double getPositionCoefficient() {
        return positionCoefficient;
    }

    public boolean isManager() {
        return isManager;
    }
}