package io.laurabrehm.dkbtechassignment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
class DkbtechassignmentApplication

fun main(args: Array<String>) {
	runApplication<DkbtechassignmentApplication>(*args)
}

@Configuration
class SwaggerConfig {
	@Bean
	fun api(): Docket {
		return Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("io.laurabrehm.dkbtechassignment"))
			.paths(PathSelectors.any())
			.build()
	}
}