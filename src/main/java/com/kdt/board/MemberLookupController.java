package com.kdt.board;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// ID로 회원 한 명을 조회하는 컨트롤러
@RestController
public class MemberLookupController {
    private final MemberService memberService;

    // 스프링이 MemberService를 자동으로 넣어주는 생성자예요(의존성 주입).
    public MemberLookupController(MemberService memberService) {
        this.memberService = memberService;
    }

    // /members/sample: ID 1번 회원을 조회해 응답 (없으면 "missing")
    @GetMapping("/members/sample")
    public String sample() {
        Long memberId = 1L; // 확인한 실제 kdt01의 ID로 변경합니다.
        Member member = this.memberService.findById(memberId);
        if (member == null) {
            return "missing";
        }
        return member.getId() + " "
                + member.getLoginId() + " "
                + member.getNickname();
    }
}