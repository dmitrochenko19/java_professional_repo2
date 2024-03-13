package ru.otus.notes;

import org.junit.jupiter.api.Test;
import ru.otus.entities.notes.AbstractNote;
import ru.otus.entities.notes.Note1000;
import ru.otus.entities.notes.Note500;

import org.junit.jupiter.api.Assertions;

import java.util.List;

public class Note1000Test {
    @Test
    public void changeTest() {
        AbstractNote note = new Note1000();
        List<AbstractNote> result = note.change();
        Assertions.assertEquals(List.of(new Note500(), new Note500()), result);
    }
}
