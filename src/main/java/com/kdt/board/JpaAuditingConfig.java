package com.kdt.board;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// JPA Auditing 활성화 설정: @CreatedDate, @LastModifiedDate가 자동으로 채워짐
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

}
