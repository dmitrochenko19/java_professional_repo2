package ru.otus.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;

public class ThrowExceptionProcessorTest {

    @Test
    @DisplayName("Тестируем процессор, который выбрасывает исключение в чётную секунду")
    void handleProcessorsTest() {
        TimeProvider timeProvider = Mockito.mock(TimeProvider.class);
        ThrowExceptionProcessor exceptionProcessor = new ThrowExceptionProcessor(timeProvider);
        var message = new Message.Builder(1L).field7("field7").build();

        Mockito.when(timeProvider.getSecond()).thenReturn(2);
        Assertions.assertThrows(RuntimeException.class, () -> exceptionProcessor.process(message));

        Mockito.when(timeProvider.getSecond()).thenReturn(3);
        Assertions.assertEquals(message, exceptionProcessor.process(message));
    }
}
