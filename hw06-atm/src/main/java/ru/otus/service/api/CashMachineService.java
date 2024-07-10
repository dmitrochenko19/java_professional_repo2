package ru.otus.service.api;

import java.util.Map;

public interface CashMachineService {
    Map<Integer, Integer> putMoney(Map<Integer, Integer> money);

    Map<Integer, Integer> getMoney(int sum);

    int checkBalance();
}
