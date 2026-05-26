package desafio.url_shortener.controller;

import desafio.url_shortener.database.model.UrlEntity;
import desafio.url_shortener.exception.NotFoundException;
import desafio.url_shortener.exception.UrlAlreadyExistsException;
import desafio.url_shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUrl(@RequestBody UrlEntity urlEntity) throws UrlAlreadyExistsException {
        urlService.createUrl(urlEntity);
    }

    @GetMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    public String getShortUrl(@PathVariable String shortUrl) throws NotFoundException {
        return urlService.getShortUrl(shortUrl);
    }

    @DeleteMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShortUrl(@PathVariable String shortUrl) throws NotFoundException {
        urlService.deleteUrl(shortUrl);
    }

}
