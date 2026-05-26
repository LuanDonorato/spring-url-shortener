package desafio.url_shortener.database.repository;

import desafio.url_shortener.database.model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUrlRepository extends JpaRepository<UrlEntity, Long> {

    Optional<UrlEntity> findByUrl(String url);
    Optional<UrlEntity> findByShortUrl(String shortUrl);
    void deleteByShortUrl(String shortUrl);
}
