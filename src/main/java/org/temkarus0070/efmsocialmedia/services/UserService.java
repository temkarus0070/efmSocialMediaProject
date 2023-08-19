package org.temkarus0070.efmsocialmedia.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.temkarus0070.efmsocialmedia.entities.Relationship;
import org.temkarus0070.efmsocialmedia.entities.RelationshipId;
import org.temkarus0070.efmsocialmedia.repositories.RelationshipRepository;
import org.temkarus0070.efmsocialmedia.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private RelationshipRepository relationshipRepository;
    private UserRepository userRepository;

    @Transactional
    public void sendFriendRequest(String friendUsername) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        Relationship relationshipToFriend = new Relationship(new RelationshipId(currentUser.getName(), friendUsername), null, null, true);
        Relationship relationshipFromFriend = new Relationship(new RelationshipId(friendUsername, currentUser.getName()), null, null, false);
        relationshipRepository.saveAll(List.of(relationshipToFriend, relationshipFromFriend));
    }

    @Transactional
    public void acceptFriendRequest(String friendUsername) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        Optional<Relationship> friendshipRequest = relationshipRepository.findById(new RelationshipId(friendUsername, currentUser.getName()));
        if (friendshipRequest.isPresent()) {
            Relationship relationshipFromFriend = friendshipRequest.get();
            relationshipFromFriend.setFriendshipAccepted(true);
            Relationship relationshipToFriend = new Relationship(new RelationshipId(currentUser.getName(), friendUsername), null, null, true);
            relationshipRepository.save(relationshipToFriend);
            relationshipRepository.saveAll(List.of(relationshipToFriend, relationshipFromFriend));
        } else throw new EntityNotFoundException("Не найден запрос на дружбу");
    }

    @Transactional
    public void declineFriendRequest(String friendUsername) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        Optional<Relationship> friendshipRequest = relationshipRepository.findById(new RelationshipId(friendUsername, currentUser.getName()));
        if (friendshipRequest.isPresent()) {
            relationshipRepository.deleteById(new RelationshipId(currentUser.getName(), friendUsername));
        } else throw new EntityNotFoundException("Не найден запрос на дружбу");
    }
}
