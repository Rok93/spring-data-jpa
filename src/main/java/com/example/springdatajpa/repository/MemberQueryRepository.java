package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository { // 스프링 빈으로 등록해서 직접 사용해도 된다!

    private final EntityManager em;

    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m")
            .getResultList();
    }

}
