package MailTestHelper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kenji on 2016/07/28.
 */
@RestController
@Slf4j
public class ErrorController {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new MyCustomizer();
    }

    private static class MyCustomizer implements EmbeddedServletContainerCustomizer {

        @Override
        public void customize(ConfigurableEmbeddedServletContainer factory) {
            //factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
            //factory.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/401"));
            factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
            //factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
        }

    }

    @RequestMapping("/404")
    public String notFoundError() {
        return "error/404";
    }
}
