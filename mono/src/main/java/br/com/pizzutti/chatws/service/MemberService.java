package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.component.TimeComponent;
import br.com.pizzutti.chatws.enums.FilterOperationEnum;
import br.com.pizzutti.chatws.model.Member;
import br.com.pizzutti.chatws.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService extends FilterService<Member>{

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
                .createdAt(TimeComponent.getInstance().now())
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
