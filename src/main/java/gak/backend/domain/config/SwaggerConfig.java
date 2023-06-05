package gak.backend.domain.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
 도메인 추가 되면 아래와 같이 method를 추가.
* */
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi memberOpenApi() {
        String[] paths = {"/member/**"};
        return GroupedOpenApi.builder().group("member").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi formOpenApi() {
        String[] paths = {"/form/**"};
        return GroupedOpenApi.builder().group("form").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi questionOpenApi() {
        String[] paths = {"/question/**"};
        return GroupedOpenApi.builder().group("question").pathsToMatch(paths)
                .build();
    }
    @Bean
    public GroupedOpenApi selectionOpenApi() {
        String[] paths = {"/selection/**"};
        return GroupedOpenApi.builder().group("selection").pathsToMatch(paths)
                .build();
    }
    @Bean
    public GroupedOpenApi descriptionOpenApi() {
        String[] paths = {"/description/**"};
        return GroupedOpenApi.builder().group("description").pathsToMatch(paths)
                .build();
    }
    @Bean
    public GroupedOpenApi responseOpenApi() {
        String[] paths = {"/response/**"};
        return GroupedOpenApi.builder().group("response").pathsToMatch(paths)
                .build();
    }
//    @Bean
//    public GroupedOpenApi sectionOpenApi() {
//        String[] paths = {"/section/**"};
//        return GroupedOpenApi.builder().group("section").pathsToMatch(paths)
//                .build();
//    }it
    @Bean
    public GroupedOpenApi filesOpenApi() {
        String[] paths = {"/files/**"};
        return GroupedOpenApi.builder().group("files").pathsToMatch(paths)
                .build();
    }


}




