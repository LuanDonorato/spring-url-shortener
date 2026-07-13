package desafio.url_shortener.controller;

import desafio.url_shortener.database.model.UrlEntity;
import desafio.url_shortener.exception.NotFoundException;
import desafio.url_shortener.exception.UrlAlreadyExistsException;
import desafio.url_shortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/urls")
@RequiredArgsConstructor
@Tag(name = "URLs", description = "Operações para gerenciamento de URLs encurtadas")
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar uma URL encurtada", description = "Recebe uma URL e gera uma versão encurtada")
    public String createUrl(@RequestBody UrlEntity urlEntity) throws UrlAlreadyExistsException {
        return urlService.createUrl(urlEntity);
    }

    @GetMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Bucar URL original", description = "Retorna a URL original a partir da URL encurtada")
    public String getShortUrl(@PathVariable String shortUrl) throws NotFoundException {
        return urlService.getShortUrl(shortUrl);
    }

    @DeleteMapping("/{shortUrl}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover URL", description = "Remove uma URL cadastrada a partir da URL encurtada")
    public void deleteShortUrl(@PathVariable String shortUrl) throws NotFoundException {
        urlService.deleteUrl(shortUrl);
    }

}
