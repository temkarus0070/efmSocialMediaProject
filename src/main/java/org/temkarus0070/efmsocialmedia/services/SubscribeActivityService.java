package org.temkarus0070.efmsocialmedia.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.temkarus0070.efmsocialmedia.entities.Post;
import org.temkarus0070.efmsocialmedia.repositories.PostRepository;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@AllArgsConstructor
public class SubscribeActivityService {
    private static final int MAX_PAGE_SIZE = 10000;
    private PostRepository postRepository;
    private UserService userService;

    public Page<Post> getLastPosts(PageRequest pageRequest) {
        if (pageRequest.getPageSize() > MAX_PAGE_SIZE)
            throw new InvalidParameterException(String.format("превышен максимальный размер страницы, максимальный размер =%d",
                    MAX_PAGE_SIZE));
        List<String> subscribeUsernameList = userService.getSubscribeWithFriendsList();
        return postRepository.findAllByAuthor_UsernameIn(subscribeUsernameList, pageRequest);
    }
}
