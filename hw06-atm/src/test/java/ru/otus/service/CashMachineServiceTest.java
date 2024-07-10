package ru.otus.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.entities.notes.Notes;
import ru.otus.service.api.CashMachineService;
import ru.otus.service.impl.CashMachineServiceImpl;

import java.util.HashMap;
import java.util.Map;


public class CashMachineServiceTest {
    @Test
    public void putMoneyTest() {
        Map<Integer, Integer> moneyBox = new HashMap<>();
        moneyBox.put(5000, 2);
        moneyBox.put(1000, 3);
        moneyBox.put(500, 2);
        moneyBox.put(100, 1);
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Map<Integer, Integer> money = new HashMap<>();
        money.put(5000, 1);
        money.put(1000, 1);
        money.put(500, 1);
        money.put(100, 2);
        cashMachineService.putMoney(money);
        Assertions.assertEquals(20800, cashMachineService.checkBalance());
    }

    @Test
    public void putMoneyNegativeTest() {
        Map<Integer, Integer> moneyBox = new HashMap<>();
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Map<Integer, Integer> money = new HashMap<>();
        money.put(2000, 1);
        Map<Integer, Integer> returnedMoneyActual = cashMachineService.putMoney(money);
        Map<Integer, Integer> returnedMoneyExpected = new HashMap<>();
        returnedMoneyExpected.put(2000, 1);
        Assertions.assertEquals(returnedMoneyExpected, returnedMoneyActual);
    }

    @Test
    public void putMoneyNotSupportedNoteTest() {
        Map<Integer, Integer> moneyBox = new HashMap<>();
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Map<Integer, Integer> money = new HashMap<>();
        money.put(5000, -1);
        money.put(1000, 1);
        money.put(500, 1);
        money.put(100, -2);
        Throwable exp = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cashMachineService.putMoney(money));
        Assertions.assertEquals(exp.getMessage(), "You can't put negative amount of notes");
    }

    @Test
    public void getMoneyTest() {
        Map<Integer, Integer> moneyBox = new HashMap<>();
        moneyBox.put(5000, 2);
        moneyBox.put(1000, 3);
        moneyBox.put(500, 2);
        moneyBox.put(100, 1);
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Map<Integer, Integer> actual = cashMachineService.getMoney(6600);

        Map<Integer, Integer> expected = Map.of(Notes.NOTE_5000.getValue(), 1, Notes.NOTE_1000.getValue(),
                1, Notes.NOTE_500.getValue(), 1, Notes.NOTE_100.getValue(), 1);
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(7500, cashMachineService.checkBalance());
    }

    @Test
    public void getMoneyTooMuchMoneyTest() {
        Map<Integer, Integer> moneyBox = new HashMap<>();
        moneyBox.put(5000, 2);
        moneyBox.put(1000, 3);
        moneyBox.put(500, 2);
        moneyBox.put(100, 1);
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Throwable exp = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cashMachineService.getMoney(16600));
        Assertions.assertEquals(exp.getMessage(), "Not enough money");

        Assertions.assertEquals(14100, cashMachineService.checkBalance());
    }

    @Test
    public void checkBalanceTest() {
        Map<Integer, Integer> moneyBox = Map.of(Notes.NOTE_5000.getValue(), 2, Notes.NOTE_1000.getValue(),
                3, Notes.NOTE_500.getValue(),2, Notes.NOTE_100.getValue(), 1);
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Assertions.assertEquals(14100, cashMachineService.checkBalance());
    }
}
