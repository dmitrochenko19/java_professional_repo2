package ru.otus.notes;

import ru.otus.entities.notes.AbstractNote;
import ru.otus.entities.notes.Note100;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Note100Test {
    @Test
    public void changeTest() {
        AbstractNote note = new Note100();
        List<AbstractNote> result = note.change();
        Assertions.assertEquals(List.of(new Note100()), result);
    }
}
