package br.com.pizzutti.chatinho.api.domain.member;

import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterService;
import org.springframework.stereotype.Service;

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
}
