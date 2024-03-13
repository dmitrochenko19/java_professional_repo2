package ru.otus.notes;

import org.junit.jupiter.api.Test;
import ru.otus.entities.notes.AbstractNote;
import ru.otus.entities.notes.Note100;
import ru.otus.entities.notes.Note500;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class Note500Test {
    @Test
    public void changeTest() {
        AbstractNote note = new Note500();
        List<AbstractNote> result = note.change();
        Assertions.assertEquals(List.of(new Note100(), new Note100(), new Note100(), new Note100(), new Note100()), result);
    }
}
