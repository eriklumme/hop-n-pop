package org.vaadin.erik;

import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.ContainerInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.vaadin.erik.game.server.Server;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ContainerInitializer atmosphereInitializer() {
        return new ContainerInitializer();
    }

    @Bean
    public ServletRegistrationBean<AtmosphereServlet> atmosphereServlet() {
        ServletRegistrationBean<AtmosphereServlet> registration = new ServletRegistrationBean<>(
                new AtmosphereServlet(), "/game/*");
        registration.addInitParameter("org.atmosphere.cpr.packages", "org.vaadin.erik.game");
        registration.setLoadOnStartup(0);
        // Need to occur before the ContainerInitializer
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
