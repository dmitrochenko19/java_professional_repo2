package ru.otus.service.api;

import ru.otus.entities.machine.MoneyBox;
import ru.otus.entities.notes.AbstractNote;

import java.util.List;

public interface MoneyBoxService {
    void getNotes(List<AbstractNote> listOfNotes);

    void putNotes(List<AbstractNote> listOfNotes);

    int getBalance();

    void changeMoneyBox(MoneyBox moneyBox);

    MoneyBox getMoneyBox();
}
