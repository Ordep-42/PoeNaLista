package br.ufrn.poenalista.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Exercita o fluxo Post/Redirect/Get sobre HTTP real (Tomcat embutido), o que o MockMvc não
 * cobre: o redirect não pode levar {@code ;jsessionid=...} na URL — senão {@code GET /} não
 * casa com o {@code HomeController} e a página quebra.
 *
 * <p>Usa só um POST inválido, que não grava itens (este teste não roda em transação).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FluxoPostRedirectGetHttpTest {

	@Value("${local.server.port}")
	private int port;

	private final HttpClient http = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();

	@Test
	void redirectAposPostNaoExpoeSessaoNaUrlEExibeMensagemFlash() throws Exception {
		HttpResponse<String> post = http.send(HttpRequest.newBuilder(uri("/itens"))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(HttpRequest.BodyPublishers.ofString("name=&quantity=0&categoryId="))
				.build(), HttpResponse.BodyHandlers.ofString());

		assertThat(post.statusCode()).isEqualTo(302);
		String location = post.headers().firstValue("Location").orElseThrow();
		assertThat(location).doesNotContainIgnoringCase("jsessionid");
		String cookie = post.headers().firstValue("Set-Cookie").orElseThrow().split(";", 2)[0];

		HttpResponse<String> get = http.send(HttpRequest.newBuilder(URI.create(location).isAbsolute()
				? URI.create(location) : uri(location))
				.header("Cookie", cookie)
				.GET()
				.build(), HttpResponse.BodyHandlers.ofString());

		assertThat(get.statusCode()).isEqualTo(200);
		assertThat(get.body())
				.contains("Lista de Compras")
				.contains("Não foi possível adicionar o item")
				.contains("Informe o nome do item.");
	}

	private URI uri(String path) {
		return URI.create("http://localhost:" + port + path);
	}

}

