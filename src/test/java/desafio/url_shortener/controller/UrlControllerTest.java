package desafio.url_shortener.controller;

import desafio.url_shortener.database.model.UrlEntity;
import desafio.url_shortener.exception.NotFoundException;
import desafio.url_shortener.exception.UrlAlreadyExistsException;
import desafio.url_shortener.service.UrlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
@AutoConfigureMockMvc
class UrlControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UrlService service;

    private UrlEntity entity;

    @Test
    void deveriaDevolverCodigo201ParaCriacaoDeUrlSemErro() throws Exception {

        String json = """
                {
                  "url": "https://www.example.com/some/long/url"
                }
                """;

        var response = mvc.perform(
                MockMvcRequestBuilders.post("/v1/urls")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    void deveriaDevolverUrlAlreadyExistsExceptionParaCriacaoDeUrlComErro() throws Exception {

        String json = """
                {
                  "url": "https://www.example.com/some/long/url"
                }
                """;

        BDDMockito.willThrow(new UrlAlreadyExistsException("Url já cadastrada"))
                .given(service)
                .createUrl(any(UrlEntity.class));

        var response = mvc.perform(
                post("/v1/urls")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains("Url já cadastrada"));
    }

    @Test
    void deveriaDevolverCodigo400ParaCriacaoDeUrlComErro() throws Exception {

        String json = """
                """;

        var response = mvc.perform(
                MockMvcRequestBuilders.post("/v1/urls")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaBuscarSemErro() throws Exception {

        this.entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        BDDMockito.given(service.getShortUrl("yZ8JN")).willReturn(entity.getUrl());

        var response = mvc.perform(
                MockMvcRequestBuilders.get("/v1/urls/{shortUrl}", "yZ8JN")
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("https://www.example.com/some/long/url", response.getContentAsString());
    }

    @Test
    void deveriaDevolverCodigo404ParaBuscarComErro() throws Exception {

        this.entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        BDDMockito.willThrow(new NotFoundException("Not found"))
                .given(service)
                .getShortUrl("yZ8JN");

        var response = mvc.perform(
                MockMvcRequestBuilders.get("/v1/urls/{shortUrl}", "yZ8JN")
        ).andReturn().getResponse();

        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo204ParaRemoverSemErro() throws Exception {

        this.entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        var response = mvc.perform(
                MockMvcRequestBuilders.delete("/v1/urls/{shortUrl}", "yZ8JN")
        ).andReturn().getResponse();

        Assertions.assertEquals(204, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo404ParaRemoverComErro() throws Exception {

        this.entity = new UrlEntity(1L, "https://www.example.com/some/long/url", "yZ8JN");

        BDDMockito.willThrow(new NotFoundException("Url não cadastrada"))
                .given(service)
                .deleteUrl("yZ8JN");

        var response = mvc.perform(
                MockMvcRequestBuilders.delete("/v1/urls/{shortUrl}", "yZ8JN")
        ).andReturn().getResponse();

        Assertions.assertEquals(404, response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains("Url não cadastrada"));
    }
}