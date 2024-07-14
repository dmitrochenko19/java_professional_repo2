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
        ThrowExceptionProcessor exceptionProcessor = Mockito.spy(new ThrowExceptionProcessor());
        var message = new Message.Builder(1L).field7("field7").build();

        Mockito.doReturn(2).when(exceptionProcessor).getSecond();
        Assertions.assertThrows(RuntimeException.class, () -> exceptionProcessor.process(message));

        Mockito.doReturn(3).when(exceptionProcessor).getSecond();
        Assertions.assertEquals(message, exceptionProcessor.process(message));
    }
}
