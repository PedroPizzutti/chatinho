package br.com.pizzutti.chatinho.api.domain.member;

import br.com.pizzutti.chatinho.api.infra.service.FilterDirectionEnum;
import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MemberService extends FilterService<Member> {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        super();
        this.memberRepository = memberRepository;
    }

    public <U> MemberService filter(String property, U value, FilterOperationEnum operation) {
        super.filter(property, value, operation);
        return this;
    }

    public MemberService orderBy(String property, FilterDirectionEnum direction) {
        super.orderBy(property, direction);
        return this;
    }

    public Member create(Long idRoom, Long idUser) {
        var member = Member.builder()
                .idUser(idUser)
                .idRoom(idRoom)
                .createdAt(TimeService.now())
                .build();
        this.memberRepository.saveAndFlush(member);
        return member;
    }

    public List<Member> find() {
        try {
            return this.memberRepository.findAll(super.specification(), super.sort());
        } finally {
            super.reset();
        }
    }

    public Member findOne() {
        try {
            return this.memberRepository.findOne(super.specification())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado!"));
        } finally {
            super.reset();
        }
    }

    public void delete(Long id) {
        this.memberRepository.deleteById(id);
    }
}
