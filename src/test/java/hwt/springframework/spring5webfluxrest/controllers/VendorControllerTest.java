package hwt.springframework.spring5webfluxrest.controllers;

import hwt.springframework.spring5webfluxrest.domain.Vendor;
import hwt.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class VendorControllerTest {
    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void vendorsList() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Biden").lastName("Joe").build()
                ,Vendor.builder().firstName("kamala").lastName("Harris").build()));

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getVendorById() {
        given(vendorRepository.findById("vendorid"))
                .willReturn(Mono.just(Vendor.builder().id("vendorid").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/vendorid")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void create() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> venToSave = Mono.just(Vendor.builder().firstName("haling").lastName("win").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(venToSave, Vendor.class)
                .exchange()
                .expectStatus().isCreated();

    }

    @Test
    void update() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToUpdate = Mono.just(Vendor.builder().firstName("haling").lastName("win").build());

        webTestClient.put()
                .uri("/api/v1/vendors/ssss")
                .body(venToUpdate, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void patchWithChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToUpdate = Mono.just(Vendor.builder().firstName("Jim").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/ssss")
                .body(venToUpdate, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    void patchWithNoChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Jim").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToUpdate = Mono.just(Vendor.builder().firstName("Jim").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/ssss")
                .body(venToUpdate, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        verify(vendorRepository, never()).save(any());
    }
}