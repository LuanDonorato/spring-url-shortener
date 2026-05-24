package desafio.url_shortener.service;

import desafio.url_shortener.database.model.UrlEntity;
import desafio.url_shortener.database.repository.IUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlService {

    public final IUrlRepository urlRepository;

    public void createUrl(UrlEntity urlEntity) {
        Optional<UrlEntity> url = urlRepository.findByUrl(urlEntity.getUrl());

        if (url.isPresent()) {
            throw new RuntimeException("Url já cadastrada");
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

    public String getShortenUrl(UrlEntity urlEntity) {
        Optional<UrlEntity> url = urlRepository.findByShortUrl(urlEntity.getShortUrl());

        if (url.isEmpty()) {
            throw new RuntimeException("Url não cadastrada");
        }

        return "https://xxx.com/" + urlEntity.getUrl();
    }
}
