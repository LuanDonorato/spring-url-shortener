package desafio.url_shortener.service;

import desafio.url_shortener.database.model.UrlEntity;
import desafio.url_shortener.database.repository.IUrlRepository;
import desafio.url_shortener.exception.NotFoundException;
import desafio.url_shortener.exception.UrlAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    IUrlRepository repository;

    @InjectMocks
    UrlService service;

    @Test
    void deveriaCriarUrlQuandoNaoExistir() throws UrlAlreadyExistsException {

        UrlEntity entity = new UrlEntity(1L, "https://www.example.com/some/long/url", null);

        BDDMockito.given(repository.findByUrl(entity.getUrl())).willReturn(Optional.empty());

        service.createUrl(entity);

        Assertions.assertNotNull(entity.getShortUrl());
        Assertions.assertEquals(5, entity.getShortUrl().length());
        BDDMockito.then(repository).should().save(entity);
    }

    @Test
    void deveriaRetornarUrlAlreadyExistsExceptionAoTentarCriarUrl() throws UrlAlreadyExistsException {

        UrlEntity entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        BDDMockito.given(repository.findByUrl(entity.getUrl())).willReturn(Optional.of(entity));

        UrlAlreadyExistsException exception = Assertions.assertThrows(
                UrlAlreadyExistsException.class, () -> service.createUrl(entity));

        Assertions.assertEquals("Url já cadastrada", exception.getMessage());
    }

    @Test
    void deveriaRetornarShortenUrl() {

        String shortUrl = service.shortenUrl();

        Assertions.assertNotNull(shortUrl);
        Assertions.assertEquals(5, shortUrl.length());
    }

    @Test
    void deveriaRetornarShortUrlValida() throws NotFoundException {

        UrlEntity entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        BDDMockito.given(repository.findByShortUrl(
                entity.getShortUrl())).willReturn(Optional.of(entity));

        Assertions.assertEquals(entity.getUrl(), service.getShortUrl(entity.getShortUrl()));
    }

    @Test
    void deveriaRetornarNotFoundExceptionAoTentarRetornarShortUrl() throws NotFoundException {

        UrlEntity entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        BDDMockito.given(repository.findByShortUrl(
                entity.getShortUrl())).willReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class, () -> service.getShortUrl(entity.getShortUrl()));

        Assertions.assertEquals("Url não cadastrada", exception.getMessage());
    }

    @Test
    void deveriaDeletarUrlValida() throws NotFoundException {

        UrlEntity entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        BDDMockito.given(repository.findByShortUrl(entity.getShortUrl())).willReturn(Optional.of(entity));

        service.deleteUrl(entity.getShortUrl());

        BDDMockito.then(repository).should().deleteByShortUrl(entity.getShortUrl());
    }

    @Test
    void deveriaRetornarNotFoundExceptionAoTentarDeletar() throws NotFoundException {

        UrlEntity entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        BDDMockito.given(repository.findByShortUrl(entity.getShortUrl())).willReturn(Optional.empty());



        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class, () -> service.deleteUrl(entity.getShortUrl()));

        Assertions.assertEquals("Url não cadastrada", exception.getMessage());

    }

}