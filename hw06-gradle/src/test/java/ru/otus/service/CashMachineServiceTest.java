package ru.otus.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.entities.machine.MoneyBox;
import ru.otus.entities.notes.Notes;
import ru.otus.service.api.CashMachineService;
import ru.otus.service.impl.CashMachineServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class CashMachineServiceTest {
    @Test
    public void putMoneyTest()
    {
        MoneyBox moneyBox = new MoneyBox(2,3,2,1);
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        cashMachineService.putMoney(1,1,1,2);
        Assertions.assertEquals(moneyBox.getNote100(), 2);
        Assertions.assertEquals(moneyBox.getNote500(), 3);
        Assertions.assertEquals(moneyBox.getNote1000(), 4);
        Assertions.assertEquals(moneyBox.getNote5000(), 4);
    }

    @Test
    public void putMoneyNegativeTest()
    {
        MoneyBox moneyBox =mock(MoneyBox.class);
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Throwable exp = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cashMachineService.putMoney(-1, 1, 1, -2));
        Assertions.assertEquals(exp.getMessage(), "You can't put negative amount of notes");
    }

    @Test
    public void getMoneyTest()
    {
        MoneyBox moneyBox = new MoneyBox(2,3,2,1);
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Map<String, Integer> actual = cashMachineService.getMoney(6600);

        Map<String, Integer> expected = new HashMap<>();
        expected.put(Notes.NOTE_5000.name(), 1);
        expected.put(Notes.NOTE_1000.name(), 1);
        expected.put(Notes.NOTE_500.name(),1);
        expected.put(Notes.NOTE_100.name(), 1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void checkBalanceTest() {
        MoneyBox moneyBox = new MoneyBox(2,3,2,1);
        CashMachineService cashMachineService = new CashMachineServiceImpl(moneyBox);
        Assertions.assertEquals(14100, cashMachineService.checkBalance());
    }
}
