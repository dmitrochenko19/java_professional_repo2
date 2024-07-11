package ru.otus.service.impl;

import ru.otus.entities.notes.Notes;
import ru.otus.service.api.CashMachineService;

import java.util.*;

public class CashMachineServiceImpl implements CashMachineService {

    private final Map<Integer, Integer> moneyBox;

    public CashMachineServiceImpl(Map<Integer, Integer> moneyBox) {
        this.moneyBox = moneyBox;
    }

    @Override
    public Map<Integer, Integer> putMoney(Map<Integer, Integer> money) {
        if (money.isEmpty()){
            return money;
        }
        for (var note : Notes.values()) {
            Integer amountOfNotes = money.get(note.getValue());
            if (amountOfNotes == null) {
                continue;
            }
            if (amountOfNotes < 0) {
                throw new IllegalArgumentException("You can't put negative amount of notes");
            }
            Integer currentAmountOfNotes = moneyBox.get(note.getValue());
            moneyBox.put(note.getValue(), currentAmountOfNotes != null ? currentAmountOfNotes + amountOfNotes : amountOfNotes);
            money.remove(note.getValue());
        }
        return money;
    }

    @Override
    public Map<Integer, Integer> getMoney(int sum) {
        if (sum < 0) {
            throw new IllegalArgumentException("sum can't be negative");
        }
        Map<Integer, Integer> result = new HashMap<>();
        try {
            for (var note : Notes.values()) {
                while (sum - note.getValue() >= 0 && moneyBox.get(note.getValue()) != null
                        && moneyBox.get(note.getValue()) > 0) {
                    sum -= note.getValue();
                    Integer noteValue = note.getValue();
                    result.put(noteValue, result.get(noteValue) != null ? result.get(noteValue) + 1 : 1);
                    Integer currentValue = moneyBox.get(note.getValue());
                    moneyBox.put(note.getValue(), currentValue - 1);
                }
            }
            if (sum > 0) {
                throw new IllegalArgumentException("Not enough money");
            }
        } catch (IllegalArgumentException e) {
            moneyBox.putAll(result);
            throw e;
        }
        return result;
    }

    @Override
    public int checkBalance() {
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : moneyBox.entrySet()) {
            if (entry.getValue() != null) {
                sum = sum + (entry.getKey() * entry.getValue());
            }
        }
        return sum;
    }
}
