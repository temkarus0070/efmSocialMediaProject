package org.temkarus0070.efmsocialmedia.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RelationshipId implements Serializable {
    private String friendRequesterUsername;
    private String friendUsername;
}
