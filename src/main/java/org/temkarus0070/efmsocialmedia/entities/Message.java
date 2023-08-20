package org.temkarus0070.efmsocialmedia.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {

    @EmbeddedId
    private MessageId id;
    @OneToOne
    private User sender;
    @OneToOne
    private User messageDestination;
    @Lob
    private String text;
}
