package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.enums.FilterDirectionEnum;
import br.com.pizzutti.chatws.enums.FilterOperationEnum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class FilterService<T> {

    private Specification<T> specification;
    private Sort sort;

    public FilterService() {
        this.specification = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        this.sort = Sort.unsorted();
    }

    protected <U> FilterService<T> filter(String property, U value, FilterOperationEnum operation) {
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

    protected Specification<T> getSpecification() {
        return this.specification;
    }

    protected Sort getSort() {
        return this.sort;
    }

    protected void reset() {
        this.specification = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        this.sort = Sort.unsorted();
    }

}
