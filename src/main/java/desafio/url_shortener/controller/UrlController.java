package desafio.url_shortener.controller;

import desafio.url_shortener.database.model.UrlEntity;
import desafio.url_shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/shorten-url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createUrl(@RequestBody UrlEntity urlEntity) {
        urlService.createUrl(urlEntity);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getShortenUrl(@RequestBody UrlEntity urlEntity) {
        return urlService.getShortenUrl(urlEntity);
    }

}
