package ru.otus.service.impl;

import ru.otus.entities.machine.MoneyBox;
import ru.otus.entities.notes.Notes;
import ru.otus.service.api.CashMachineService;

import java.util.HashMap;
import java.util.Map;

public class CashMachineServiceImpl implements CashMachineService {

    private final MoneyBox moneyBox;

    public CashMachineServiceImpl(MoneyBox moneyBox) {
        this.moneyBox = moneyBox;
    }

    @Override
    public void putMoney(int note100, int note500, int note1000, int note5000) {
        if (note100 < 0 || note500<0 || note1000<0 || note5000<0)
        {
            throw new IllegalArgumentException("You can't put negative amount of notes");
        }
        moneyBox.setNote100(moneyBox.getNote100()+note100);
        moneyBox.setNote500(moneyBox.getNote500()+note500);
        moneyBox.setNote1000(moneyBox.getNote1000()+note1000);
        moneyBox.setNote5000(moneyBox.getNote5000()+note5000);
    }

    @Override
    public Map<String, Integer> getMoney(int sum) {
        if (sum < 0)
        {
            throw new IllegalArgumentException("sum can't be negative");
        }
        Map<String, Integer> result = new HashMap<>();
        for(var note: Notes.values())
        {
            while (sum-note.getValue()>=0)
            {
                sum-=note.getValue();
                result.put(note.name(), result.get(note.name()) != null ? result.get(note.name())+1 : 1);
            }
        }
        if (sum > 0)
        {
            throw new IllegalArgumentException("Not enough money");
        }
        return result;
    }

    @Override
    public int checkBalance() {
        return moneyBox.getNote100()*Notes.NOTE_100.getValue()+ moneyBox.getNote500()*Notes.NOTE_500.getValue()+
                moneyBox.getNote1000()*Notes.NOTE_1000.getValue()+ moneyBox.getNote5000()*Notes.NOTE_5000.getValue();
    }
}
