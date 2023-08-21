package org.temkarus0070.efmsocialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private long id;

    @NotNull(message = "Текст поста не должен быть равным null")
    @NotBlank(message = "Текст поста не может быть пустым полем")
    private String text;
    @NotNull(message = "Заголовок поста не должен быть равным null")
    @NotBlank(message = "Заголовок поста не может быть пустым полем")
    private String header;
    private LocalDateTime created;
    private List<ImageDto> images = new ArrayList<>();
    private String authorName;

}
