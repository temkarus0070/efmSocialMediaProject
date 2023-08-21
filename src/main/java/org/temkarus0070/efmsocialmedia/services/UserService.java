package org.temkarus0070.efmsocialmedia.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.temkarus0070.efmsocialmedia.entities.Relationship;
import org.temkarus0070.efmsocialmedia.entities.RelationshipId;
import org.temkarus0070.efmsocialmedia.entities.User;
import org.temkarus0070.efmsocialmedia.repositories.RelationshipRepository;
import org.temkarus0070.efmsocialmedia.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private RelationshipRepository relationshipRepository;
    private UserRepository userRepository;

    public List<String> getFriendList() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        return relationshipRepository.findRelationshipsByIdFriendRequesterUsernameAndConfirmedFriendTrue(authentication.getName())
                                     .stream()
                                     .map(e -> e.getId()
                                                .getFriendUsername())
                                     .collect(Collectors.toList());
    }

    public List<String> getFriendsRequestsList() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        return relationshipRepository.findRelationshipsByIdFriendRequesterUsernameAndConfirmedFriendFalseAndSubscribeFalse(
                                         authentication.getName())
                                     .stream()
                                     .map(e -> e.getId()
                                                .getFriendUsername())
                                     .collect(Collectors.toList());
    }

    public List<String> getSubscribersList() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        return relationshipRepository.findUserSubscribers(authentication.getName())
                                     .stream()
                                     .map(e -> e.getId()
                                                .getFriendRequesterUsername())
                                     .collect(Collectors.toList());
    }

    public List<String> getSubscribeList() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        return relationshipRepository.findRelationshipsByIdFriendRequesterUsernameAndConfirmedFriendFalseAndSubscribeTrue(
                                         authentication.getName())
                                     .stream()
                                     .map(e -> e.getId()
                                                .getFriendUsername())
                                     .collect(Collectors.toList());
    }

    private boolean doSubscriberIfFriend(Relationship friendshipRequestFromFriend, Relationship friendshipRequestToFriend) {
        if (friendshipRequestFromFriend != null && friendshipRequestFromFriend.isConfirmedFriend()) {
            friendshipRequestFromFriend.setConfirmedFriend(false);
            friendshipRequestFromFriend.setSubscribe(true);
            relationshipRepository.deleteById(friendshipRequestToFriend.getId());
            relationshipRepository.save(friendshipRequestFromFriend);
            return true;
        }
        return false;
    }

    public List<String> getSubscribeWithFriendsList() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        return relationshipRepository.findRelationshipsByIdFriendRequesterUsernameAndSubscribeTrue(authentication.getName())
                                     .stream()
                                     .map(e -> e.getId()
                                                .getFriendUsername())
                                     .collect(Collectors.toList());
    }

    @Transactional
    public void sendFriendRequest(String friendUsername) {
        Authentication currentUser = SecurityContextHolder.getContext()
                                                          .getAuthentication();
        if (userRepository.existsById(friendUsername)) {

            Relationship relationshipToFriend = new Relationship(new RelationshipId(currentUser.getName(), friendUsername),
                                                                 new User(currentUser.getName()),
                                                                 new User(friendUsername),
                                                                 false,
                                                                 true);
            Relationship relationshipFromFriend = new Relationship(new RelationshipId(friendUsername, currentUser.getName()),
                                                                   new User(friendUsername),
                                                                   new User(currentUser.getName()),
                                                                   false,
                                                                   false);
            List<Relationship> alreadyExistedRelationships =
                relationshipRepository.findAllById(List.of(relationshipToFriend.getId(), relationshipFromFriend.getId()));
            if (alreadyExistedRelationships.size() == 2) {
                throw new IllegalArgumentException("Вы уже друзья или ваша заявка еще в рассмотрении");
            } else if (!alreadyExistedRelationships.isEmpty() && alreadyExistedRelationships.stream()
                                                                                            .allMatch(r -> r.getId()
                                                                                                            .equals(
                                                                                                                relationshipFromFriend.getId()))) {
                relationshipToFriend.setConfirmedFriend(true);
                relationshipToFriend.setSubscribe(true);
                relationshipFromFriend.setConfirmedFriend(true);
            } else if (!alreadyExistedRelationships.isEmpty()) {
                throw new IllegalArgumentException("Вы были отправлены в подписчики данным пользователем");
            }
            relationshipRepository.saveAll(List.of(relationshipToFriend, relationshipFromFriend));
        } else {
            throw new EntityNotFoundException("Не найден пользователь с таким именем");
        }
    }

    @Transactional
    public void acceptFriendRequest(String friendUsername) {
        Authentication currentUser = SecurityContextHolder.getContext()
                                                          .getAuthentication();
        Optional<Relationship> friendshipRequest =
            relationshipRepository.findById(new RelationshipId(friendUsername, currentUser.getName()));
        if (friendshipRequest.isPresent()) {
            Relationship relationshipFromFriend = friendshipRequest.get();
            if (relationshipFromFriend.isConfirmedFriend()) {
                throw new IllegalArgumentException("Вы уже в статусе друзей");
            }
            relationshipFromFriend.setConfirmedFriend(true);
            relationshipFromFriend.setSubscribe(true);
            Relationship relationshipToFriend =
                new Relationship(new RelationshipId(currentUser.getName(), friendUsername), null, null, true, true);
            relationshipRepository.save(relationshipToFriend);
            relationshipRepository.saveAll(List.of(relationshipToFriend, relationshipFromFriend));
        } else {
            throw new EntityNotFoundException("Не найден запрос на дружбу");
        }
    }

    @Transactional
    public void removeFriend(String friendUsername) {
        Authentication currentUser = SecurityContextHolder.getContext()
                                                          .getAuthentication();
        Optional<Relationship> friendshipRequestFromFriendOptional =
            relationshipRepository.findById(new RelationshipId(friendUsername, currentUser.getName()));
        Optional<Relationship> friendshipRequestToFriendOptional =
            relationshipRepository.findById(new RelationshipId(currentUser.getName(), friendUsername));
        if (friendshipRequestToFriendOptional.isPresent()) {
            if (friendshipRequestFromFriendOptional.isPresent() && doSubscriberIfFriend(friendshipRequestFromFriendOptional.get(),
                                                                                        friendshipRequestToFriendOptional.get())) {
                return;
            }
            relationshipRepository.deleteAllById(List.of(new RelationshipId(currentUser.getName(), friendUsername),
                                                         new RelationshipId(friendUsername, currentUser.getName())));
        } else {
            throw new EntityNotFoundException("Не найден запрос на дружбу");
        }

    }

    public boolean hasFriend(String friendUsername) {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        return relationshipRepository.existsRelationshipByConfirmedFriendIsTrueAndIdFriendRequesterUsernameAndIdFriendUsername(
            authentication.getName(),
            friendUsername);
    }

}
