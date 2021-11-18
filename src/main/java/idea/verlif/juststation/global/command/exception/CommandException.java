package idea.verlif.juststation.global.command.exception;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/15 10:35
 */
public class CommandException extends RuntimeException {

    public CommandException(String message) {
        super(message);
    }
}