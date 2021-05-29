package hu.perit.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import hu.perit.spvitamin.spring.environment.EnvironmentPostProcessor;

@SpringBootApplication
@ComponentScan(basePackages = {"hu.perit.spvitamin", "hu.perit.orchestrator"})
public class Application
{

    public static void main(String[] args)
    {
        SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(new EnvironmentPostProcessor());
        application.run(args);
    }

}
