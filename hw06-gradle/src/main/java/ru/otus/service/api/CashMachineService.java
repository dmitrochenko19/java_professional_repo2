package ru.otus.service.api;


import  ru.otus.entities.notes.AbstractNote;

import java.util.List;

public interface CashMachineService {
    void putMoney(List<AbstractNote> listOfNotes);

    void getMoney(int sum);

    int checkBalance();
}
