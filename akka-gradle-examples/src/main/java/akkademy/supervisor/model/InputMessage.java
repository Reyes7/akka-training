package akkademy.supervisor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class InputMessage {
    Command command;
    String value;
}
