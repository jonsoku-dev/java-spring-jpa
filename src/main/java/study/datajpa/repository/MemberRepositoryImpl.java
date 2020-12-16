package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * querydsl 을 사용할때 custom 으로 활용한다.
 * 커스텀 레포지토리의 구현체는 기준이 되는 레포지토리에 Impl 을 붙히는것이 규칙
 * MemberRepository 인 경우 -> MemberRepositoryImpl
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final EntityManager em;

//    public MemberRepositoryImpl(EntityManager em) {
//        this.em = em;
//    }

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
