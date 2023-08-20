package org.temkarus0070.efmsocialmedia.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MessageId implements Serializable {

    private String senderUsername;
    private String destinationUsername;
    @CreationTimestamp
    private LocalDateTime creationTime;
}
