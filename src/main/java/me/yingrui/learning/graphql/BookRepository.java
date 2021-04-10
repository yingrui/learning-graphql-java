package me.yingrui.learning.graphql;

import me.yingrui.learning.graphql.model.Author;
import me.yingrui.learning.graphql.model.Book;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> List<T> search(String q, Class<T> clazz) {
        System.out.println(q);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        Root<T> obj = query.from(clazz);

        Path<String> fieldIdPath = obj.get("name");
        query.select(obj).where(cb.like(fieldIdPath, "%"+q+"%"));
        return entityManager.createQuery(query).getResultList();
    }

    public Book getBookById(String id) {
        return getById(id, Book.class);
    }

    public Author getAuthorById(String id) {
        return getById(id, Author.class);
    }

    private <T> T getById(String id, Class<T> clazz) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        Root<T> obj = query.from(clazz);

        Path<String> fieldIdPath = obj.get("id");
        query.select(obj).where(cb.equal(fieldIdPath, id));
        return entityManager.createQuery(query).getSingleResult();
    }

}
