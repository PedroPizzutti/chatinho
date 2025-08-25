package br.com.pizzutti.chatinho.api.infra.config.communication;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@SuperBuilder
public class PageDto<T> {
    protected List<T> data;
    protected Integer page;
    protected Integer perPage;
    protected Integer totalPages;
    protected Integer records;
    protected Long totalRecords;

    public PageDto() {};

    public PageDto(Page<T> pageable) {
        this.data = pageable.getContent();
        this.page = pageable.getNumber() + 1;
        this.perPage = pageable.getSize();
        this.totalPages = pageable.getTotalPages();
        this.records = pageable.getNumberOfElements();
        this.totalRecords = pageable.getTotalElements();
    }

    public PageDto<T> fromPage(Page<T> pageable) {
        return new PageDto<T>(pageable);
    }

}
