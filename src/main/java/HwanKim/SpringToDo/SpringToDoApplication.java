package HwanKim.SpringToDo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 기능을 활성화
@SpringBootApplication
public class SpringToDoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringToDoApplication.class, args);
	}

}
