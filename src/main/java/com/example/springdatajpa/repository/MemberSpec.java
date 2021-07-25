package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.StringUtils;

public class MemberSpec {

    public static Specification<Member> teamName(String teamName) {
        return (root, query, criteriaBuilder) -> { //root는 처음 찝은 Entity라 생각하면 된다!

            if (StringUtils.isEmpty(teamName)) { // teamName 이 empty이면 null을 반환한다.
                return null;
            }

            Join<Member, Team> memberWithTeam = root.join("team", JoinType.INNER);// 회원과 조인
            return criteriaBuilder.equal(memberWithTeam.get("name"), teamName);
        };
    }

    public static Specification<Member> userName(final String userName) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("userName"), userName);
    }
}
