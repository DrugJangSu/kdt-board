package com.kdt.board;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 메시지를 제공하는 서비스 클래스.
 *
 * 서비스(Service)는 실제 처리 로직(비즈니스 로직)을 담당하는 계층입니다.
 * 컨트롤러가 요청을 받으면, 필요한 일을 이 서비스에 시키는 구조로 많이 사용합니다.
 */
@Service // 스프링이 이 클래스를 "서비스 빈"으로 등록해서 다른 곳에서 주입(@Autowired 등)해 쓸 수 있게 합니다.
public class HelloService {

    // 호출할 때마다 고정 문자열 "kimbap"을 돌려주는 메서드
    public String message() {
        return "kimbap";
    }

    // 문자열 두 개를 받아 순서대로 리스트에 담아 돌려주는 메서드
    public List<String> message2(String a,String b){
        List<String> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        return list;
    }

}
