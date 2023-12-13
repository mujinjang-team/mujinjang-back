package mujinjang.couponsystem.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	info = @Info(title = "틀조 쿠폰시스템 API 명세서",
		version = "v1"),
	servers = {
		@Server(url = "나중에 도메인 추가", description = "테스트 서버"),
	}
)
@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi allOpenApi() {
		String[] paths = {"/**"};

		return GroupedOpenApi
			.builder()
			.group("전체 API")
			.pathsToMatch(paths)
			.build();
	}
}
