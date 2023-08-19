package org.temkarus0070.efmsocialmedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.entities.Relationship;
import org.temkarus0070.efmsocialmedia.entities.RelationshipId;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {
}
