package ru.otus;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final Lock lock = new ReentrantLock();
    private static final Condition firstCondition = lock.newCondition();
    private static final Condition secondCondition = lock.newCondition();
    private static int count = 1;
    private static boolean firstTurn = true;

    public static void main(String[] args) {
        Thread firstThread = new Thread(() -> printNumbers(true));
        Thread secondThread = new Thread(() -> printNumbers(false));

        firstThread.start();
        secondThread.start();
    }

    private static void printNumbers(boolean isFirstThread) {
        try {
            while (true) {
                lock.lock();
                try {
                    if (isFirstThread) {
                        if (!firstTurn) {
                            firstCondition.await();
                        }
                        if (count > 10) {
                            secondCondition.signal();
                            break;
                        }
                        System.out.println(Thread.currentThread().getName() + "   " + count);
                        firstTurn = false;
                        secondCondition.signal();
                    }
                    else {
                        if (firstTurn) {
                            secondCondition.await();
                        }
                        if (count > 10) {
                            firstCondition.signal();
                            break;
                        }
                        System.out.println(Thread.currentThread().getName() + "   " + count);
                        firstTurn = true;
                        count++;
                        firstCondition.signal();
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        try {
            while (true) {
                lock.lock();
                try {
                    if (isFirstThread) {
                        if (!firstTurn) {
                            firstCondition.await();
                        }
                        if (count < 3) {
                            secondCondition.signal();
                            return;
                        }
                        System.out.println(Thread.currentThread().getName() + "   " + (count - 2));
                        firstTurn = false;
                        secondCondition.signal();
                    }
                    else {
                        if (firstTurn) {
                            secondCondition.await();
                        }
                        if (count < 3) {
                            firstCondition.signal();
                            return;
                        }
                        System.out.println(Thread.currentThread().getName() + "   " + (count - 2));
                        count--;
                        firstTurn = true;
                        firstCondition.signal();
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}