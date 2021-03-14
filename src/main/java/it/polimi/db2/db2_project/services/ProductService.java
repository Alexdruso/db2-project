package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.AnswerEntity;
import it.polimi.db2.db2_project.entities.ProductEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class ProductService {

    @PersistenceContext
    private EntityManager em;

    public Optional<ProductEntity> findCurrentProduct() {
        return findByDate(new Date());
    }

    public Optional<ProductEntity> findByDate(Date date) {
        return em.createNamedQuery("Product.findByDate", ProductEntity.class)
                        .setParameter("date", date)
                        .getResultStream().findFirst();

    }

    public List<AnswerEntity> findProductReviews(ProductEntity product) {
        return em.createNamedQuery("Product.findReviewsByProduct", AnswerEntity.class)
                .setParameter("productId", product.getId())
                .getResultList();
    }

    public List<ProductEntity> findAllProducts() {
        return em.createNamedQuery("Product.findAll", ProductEntity.class).getResultList();
    }
}
