package br.com.pizzutti.chatinho.api.infra.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class FilterService<T> {

    private Specification<T> specification;
    private Sort sort;

    public FilterService() {
        this.specification = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        this.sort = Sort.unsorted();
    }

    public <U> FilterService<T> filter(String property, U value, FilterOperationEnum operation) {
        if (value == null) return this;
        if ((value instanceof String) && (((String) value).trim().isEmpty())) return this;
        if ((value instanceof Number) && (((Number) value).doubleValue() == 0)) return this;

        switch (operation) {
            case EQUAL -> this.specification =
                    this.specification.and(
                        (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(property), value)
                    );
            case LIKE -> this.specification =
                    this.specification.and(
                            (root, query, criteriaBuilder) -> criteriaBuilder.like(
                                criteriaBuilder.upper(root.get(property)), value.toString().toUpperCase() + "%"
                            )
                    );
            case IN -> {
                if (value instanceof Collection<?> collection && !collection.isEmpty()) {
                    this.specification =
                            this.specification.and(
                                ((root, query, criteriaBuilder) -> root.get(property).in(collection))
                            );
                }
            }
            case NOT_IN -> {
                if (value instanceof Collection<?> collection && !collection.isEmpty()) {
                    this.specification =
                            this.specification.and(
                                (root, query, criteriaBuilder) ->
                                    criteriaBuilder.not(root.get(property).in(collection))
                            );
                }
            }
        }
        return this;
    }

    protected FilterService<T> orderBy(String property, FilterDirectionEnum direction) {
        if (property.isBlank()) return this;
        switch (direction) {
            case ASC -> this.sort = Sort.by(property).ascending();
            case DESC -> this.sort = Sort.by(property).descending();
        }
        return this;
    }

    public Specification<T> specification() {
        return this.specification;
    }

    protected Sort sort() {
        return this.sort;
    }

    protected FilterService<T> reset() {
        this.specification = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        this.sort = Sort.unsorted();
        return this;
    }

}
