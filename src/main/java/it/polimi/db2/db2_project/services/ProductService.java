package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.AnswerEntity;
import it.polimi.db2.db2_project.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public class ProductService {

    public Optional<ProductEntity> getProductOfTheDay() {
        //TODO Check time from the server
    }

    public List<AnswerEntity> getProductReviews(ProductEntity product) {
        //TODO
    }
}
