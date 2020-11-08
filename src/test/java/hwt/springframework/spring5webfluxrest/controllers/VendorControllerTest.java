package hwt.springframework.spring5webfluxrest.controllers;

import hwt.springframework.spring5webfluxrest.domain.Vendor;
import hwt.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

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
        BDDMockito.given(vendorRepository.findAll())
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
        BDDMockito.given(vendorRepository.findById("vendorid"))
                .willReturn(Mono.just(Vendor.builder().id("vendorid").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/vendorid")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void create() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
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
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToUpdate = Mono.just(Vendor.builder().firstName("haling").lastName("win").build());

        webTestClient.put()
                .uri("/api/v1/vendors/ssss")
                .body(venToUpdate, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }
}