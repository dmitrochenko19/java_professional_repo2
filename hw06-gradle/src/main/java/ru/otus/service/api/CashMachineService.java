package ru.otus.service.api;

import java.util.Map;

public interface CashMachineService {
    void putMoney(int note100, int note500, int note1000, int note5000);

    Map<String, Integer> getMoney(int sum);

    int checkBalance();
}
