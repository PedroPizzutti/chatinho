package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.component.TimeComponent;
import br.com.pizzutti.chatws.model.Member;
import br.com.pizzutti.chatws.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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

    public List<Member> findByRoom(Long idRoom) {
        return this.memberRepository.findByIdRoom(idRoom);
    }
}
