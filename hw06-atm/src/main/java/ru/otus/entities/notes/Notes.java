package ru.otus.entities.notes;

public enum Notes {
    NOTE_5000(5000),
    NOTE_1000(1000),
    NOTE_500(500),
    NOTE_100(100);
    private final int value;

    Notes(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
