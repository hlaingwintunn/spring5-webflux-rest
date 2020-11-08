package hwt.springframework.spring5webfluxrest.bootstrap;

import hwt.springframework.spring5webfluxrest.domain.Category;
import hwt.springframework.spring5webfluxrest.domain.Vendor;
import hwt.springframework.spring5webfluxrest.repositories.CategoryRepository;
import hwt.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count().block() == 0) {
            //load data
            System.out.println("#### LOADING DATA ON BOOTSTRAP ###");

            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();

            System.out.println("Load Category :" + categoryRepository.count().block());

            vendorRepository.save(Vendor.builder()
                    .firstName("Joe")
                    .lastName("Biden").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstName("Donal")
                    .lastName("Trump").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstName("Kamalar")
                    .lastName("Harris").build()).block();

            vendorRepository.save(Vendor.builder()
                    .firstName("Mike")
                    .lastName("Pence").build()).block();

            System.out.println("Load Vendor :" + vendorRepository.count().block());
        }
    }
}
