package org.temkarus0070.efmsocialmedia.subscribeActivity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.temkarus0070.efmsocialmedia.BaseApiTest;
import org.temkarus0070.efmsocialmedia.dto.PostDto;

import java.util.List;

public class SubscribeActivityTest extends BaseApiTest {

    @Autowired
    private SubscribeActivityTestUtils subscribeActivityTestUtils;

    @Test
    public void testActivityPosts() throws Exception {
        subscribeActivityTestUtils.addTwoPersonsAsFriends("temkarus0070", "pupkin777");
        List<PostDto> pupkinPosts = subscribeActivityTestUtils.generatePostsFromPerson("pupkin777", 300);
        subscribeActivityTestUtils.checkFriendActivityIsShowed("temkarus0070", "pupkin777", pupkinPosts);
    }
}
