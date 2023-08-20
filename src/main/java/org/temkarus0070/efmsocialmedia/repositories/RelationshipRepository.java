package org.temkarus0070.efmsocialmedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.entities.Relationship;
import org.temkarus0070.efmsocialmedia.entities.RelationshipId;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {

    public List<Relationship> findRelationshipsByIdFriendRequesterUsernameAndConfirmedFriendTrue(String friendRequesterUsername);

    public List<Relationship> findRelationshipsByIdFriendRequesterUsernameAndConfirmedFriendFalseAndSubscribeTrue(String friendRequesterUsername);

    public List<Relationship> findRelationshipsByIdFriendRequesterUsernameAndSubscribeTrue(String friendRequesterUsername);

    public List<Relationship> findRelationshipsByIdFriendRequesterUsernameAndConfirmedFriendFalseAndSubscribeFalse(String friendRequesterUsername);

    public boolean existsRelationshipByConfirmedFriendIsTrueAndIdFriendRequesterUsernameAndIdFriendUsername(String userNameToCheck,
                                                                                                            String friendUsername);

    @Query(
        value = "from Relationship r where r.id.friendUsername=:username and not exists(select f from Relationship f where f.id.friendRequesterUsername=:username)")
    public List<Relationship> findUserSubscribers(@Param("username") String friendRequesterUsername);
}
