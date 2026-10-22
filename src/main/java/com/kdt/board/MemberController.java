package com.kdt.board;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 회원 조회 컨트롤러
@RestController
public class MemberController {

    private final MemberService memberService;

    // 스프링이 MemberService를 자동으로 넣어주는 생성자예요(의존성 주입).
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // /members: 전체 회원을 "ID 로그인ID 닉네임" 한 줄씩 출력
    @GetMapping("/members")
    public String members() {
        StringBuilder lines = new StringBuilder();

        for (Member member : this.memberService.findAll()) {
            lines.append(member.getId())
                    .append(" ")
                    .append(member.getLoginId())
                    .append(" ")
                    .append(member.getNickname())
                    .append("\n");
        }
        return lines.toString();
    }

    // /test/members: 닉네임으로 조회 테스트 (결과는 출력하지 않고 "ok"만 응답)
    @GetMapping("/test/members")
    public String testMembers() {
        String a = "kdt01";
        this.memberService.findByNickname(a);
        return "ok";
    }

}