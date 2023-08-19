package org.temkarus0070.efmsocialmedia.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String text;
    private String header;

    @OneToMany(mappedBy = "post")
    private List<Image> images;

    @ManyToOne
    private User author;
}
