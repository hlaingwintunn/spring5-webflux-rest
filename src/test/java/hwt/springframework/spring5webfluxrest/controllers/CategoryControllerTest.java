package hwt.springframework.spring5webfluxrest.controllers;

import hwt.springframework.spring5webfluxrest.domain.Category;
import hwt.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CategoryControllerTest {
    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                        Category.builder().description("Cat2").build()));

        webTestClient.get()
                .uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(categoryRepository.findById("someid"))
                .willReturn(Mono.just(Category.builder().description("Cat").build()));

        webTestClient.get()
                .uri("/api/v1/categories/someid")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void create() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().description("desc").build()));

        Mono<Category> catToSave = Mono.just(Category.builder().description("desc1").build());

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void update() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate = Mono.just(Category.builder().description("desc1").build());

        webTestClient.put()
                .uri("/api/v1/categories/asss")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void patchWithChanges() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("New Description").build());

        webTestClient.patch()
                .uri("/api/v1/categories/asss")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus().isOk();

        verify(categoryRepository).save(any());
    }

    @Test
    void patchWithNoChanges() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri("/api/v1/categories/asdfasdf")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus().isOk();

        verify(categoryRepository, never()).save(any());
    }

}