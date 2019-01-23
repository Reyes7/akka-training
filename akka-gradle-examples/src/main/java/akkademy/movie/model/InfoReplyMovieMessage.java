package akkademy.movie.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InfoReplyMovieMessage {

    private final String movie;
    private final Integer views;
}