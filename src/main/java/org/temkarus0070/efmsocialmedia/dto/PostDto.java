package org.temkarus0070.efmsocialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private long id;
    private String text;
    private String header;
    private LocalDateTime created;
    private List<ImageDto> images = new ArrayList<>();
    private String authorName;
}
