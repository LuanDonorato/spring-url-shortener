package desafio.url_shortener.exception;

public class UrlAlreadyExistsException extends Exception{

    public UrlAlreadyExistsException(String message) {
        super(message);
    }
}
