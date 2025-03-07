package dev.umang.productservicenov24.services;

import dev.umang.productservicenov24.exceptions.ProductNotFoundException;
import dev.umang.productservicenov24.models.Category;
import dev.umang.productservicenov24.models.Product;
import dev.umang.productservicenov24.repositories.CategoryRepository;
import dev.umang.productservicenov24.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("selfProductService")
public class SelfProductService implements ProductService{

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public SelfProductService(CategoryRepository categoryRepository,
                              ProductRepository productRepository){
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProductsPaginated(int pageNo, int pageSize){
        return productRepository.findAll(
                PageRequest.of(pageNo, pageSize,
                        Sort.by("title").descending().and(Sort.by("price").ascending()))
        );
    }

    @Override
    public Product getSingleProduct(long id) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product with id " + id + " is not found in the database");
        }
        return product.get();
    }

    @Override
    public Product createProduct(String title,
                                 String description,
                                 double price,
                                 String imageUrl,
                                 String category) {
        Product p = new Product();
        //p.setId(12);
        p.setTitle(title);
        p.setDescription(description);
        p.setPrice(price);
        p.setImageUrl(imageUrl);
        //p.setCreatedAt();

        Category categoryFromDB = categoryRepository.findByTitle(category);

        if(categoryFromDB == null){
            Category newCategory = new Category();
            newCategory.setTitle(category);
            p.setCategory(newCategory);
        }else{
            p.setCategory(categoryFromDB);
        }

        Product createdProduct = productRepository.save(p);


        return createdProduct;
    }
}
