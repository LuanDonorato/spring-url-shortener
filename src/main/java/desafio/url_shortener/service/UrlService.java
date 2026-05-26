package desafio.url_shortener.service;

import desafio.url_shortener.database.model.UrlEntity;
import desafio.url_shortener.database.repository.IUrlRepository;
import desafio.url_shortener.exception.NotFoundException;
import desafio.url_shortener.exception.UrlAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlService {

    public final IUrlRepository urlRepository;

    public void createUrl(UrlEntity urlEntity) throws UrlAlreadyExistsException{
        Optional<UrlEntity> url = urlRepository.findByUrl(urlEntity.getUrl());

        if (url.isPresent()) {
            throw new UrlAlreadyExistsException("Url já cadastrada");
        }

        urlEntity.setShortUrl(shortenUrl());

        urlRepository.save(urlEntity);
    }

    public String shortenUrl() {

        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();
        String shortUrl = "";

        for (int i = 0; i < 5; i++) {

            int index = random.nextInt(letters.length());
            shortUrl += String.valueOf(letters.charAt(index));

        }

        return shortUrl;
    }

    public String getShortUrl(String shortUrl) throws NotFoundException {

        Optional<UrlEntity> url = urlRepository.findByShortUrl(shortUrl);

        if (url.isEmpty()) {
            throw new NotFoundException("Url não cadastrada");
        }

        UrlEntity urlEntity = url.get();

        return urlEntity.getUrl();
    }

    @Transactional
    public void deleteUrl(String shortUrl) throws NotFoundException {

        Optional<UrlEntity> url = urlRepository.findByShortUrl(shortUrl);

        if (url.isEmpty()) {
            throw new NotFoundException("Url não cadastrada");
        }

        UrlEntity urlEntity = url.get();

        urlRepository.deleteByShortUrl(urlEntity.getShortUrl());
    }
}
