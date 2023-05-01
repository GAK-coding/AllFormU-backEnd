package gak.backend.domain.form.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDto {

    private Long id;

    //private FormDTO form;
    private String content;
    //private List<OptionDto> options;
    private int order;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
