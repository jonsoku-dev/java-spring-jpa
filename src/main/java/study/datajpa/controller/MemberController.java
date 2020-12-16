package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    // 도메인 클래스 컨버터 사용 - 반드시 조회용
    // 만약 id가 member의 pk면 member로 받아도 스프링 boot가 자동으로 변환하여 인젝션 해준다.
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    /**
     * /members?page=0&size=3&sort=id,desc&sort=username,desc
     * page: 현재 페이지, 0부터 시작한다.
     * size: 한 페이지에 노출할 데이터 건수
     * sort: 정렬 조건을 정의한다. 예) 정렬 속성,정렬 속성...(ASC | DESC), 정렬 방향을 변경하고 싶으면 sort
     * 파라미터 추가 ( asc 생략 가능)
     *
     * application.yml
     * @PageableDefault(size = 5, sort = "username")
     * spring.data.web.pageable.default-page-size=20 /# 기본 페이지 사이즈/
     * spring.data.web.pageable.max-page-size=2000 /# 최대 페이지 사이즈/
     *
     * 반환타입이 Page 면 totalCount query 가 같이 나간다. Slice <-> Page
     *
     * 예시같이 Entity 를 바로 노출시키면 안된다.
     */
//    public Page<Member> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
//        Page<Member> page = memberRepository.findAll(pageable);
//        return page;
//    }

    /**
     * 아래와 같이 Dto 로 반드시 변환해서 내보내는 것이 좋다 !!!!! 강조강조강조초강조
     */
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
//        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
//        return map;

//        // paging 을 커스터마이징 하고 싶을 때
//        PageRequest request = PageRequest.of(1, 2);
//        Page<MemberDto> map = memberRepository.findAll(request)
//                .map(MemberDto::new);
//
//        MyPage<MemberDto>...

                // dto 에 멤버를 바로 넣어줄 수 있다.
        return memberRepository.findAll(pageable)
                .map(member -> new MemberDto(member));
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
