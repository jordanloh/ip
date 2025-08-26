package chiikawa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import chiikawa.exception.ChiikawaException;

public class ParserTest {

    @Test
    public void parseCommand_emptyInput_exceptionThrown() {
        ChiikawaException emptyEx = assertThrows(ChiikawaException.class, () -> {
            Parser.parseCommand("");
        });
        assertEquals("Input cannot be empty!", emptyEx.getMessage());
    }

    @Test
    public void parseCommand_invalidCommand_exceptionThrown() {
        ChiikawaException invalidEx = assertThrows(ChiikawaException.class, () -> {
            Parser.parseCommand("invalid command");
        });
        assertEquals("Oh no! I don't recognise that command :(!", invalidEx.getMessage());

    }

    @Test
    public void parse_Command_validCommand_success() {
        try {
            Command result = Parser.parseCommand("todo Buy milk");
            assertEquals(Command.todo, result, "parseCommand should return Command.todo");
        } catch (ChiikawaException e) {
            fail();
        }

    }
}
