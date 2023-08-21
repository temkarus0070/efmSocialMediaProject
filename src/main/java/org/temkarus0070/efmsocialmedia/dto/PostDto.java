package org.temkarus0070.efmsocialmedia.dto;

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
    private String text;
    private String header;
    private LocalDateTime created;
    private List<ImageDto> images = new ArrayList<>();
    private String authorName;

}
