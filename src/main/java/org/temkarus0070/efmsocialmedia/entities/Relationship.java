package org.temkarus0070.efmsocialmedia.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
    @EmbeddedId
    private RelationshipId id;
    @ManyToOne
    @MapsId("friendRequesterUsername")
    @JsonIgnore
    private User friendRequester;
    @ManyToOne
    @MapsId("friendUsername")
    private User friend;

    private boolean friendshipAccepted;
}

