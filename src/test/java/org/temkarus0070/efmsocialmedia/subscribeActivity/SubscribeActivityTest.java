package org.temkarus0070.efmsocialmedia.subscribeActivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.temkarus0070.efmsocialmedia.BaseApiTest;

public class SubscribeActivityTest extends BaseApiTest {

    @Autowired
    private SubscribeActivityTestUtils subscribeActivityTestUtils;

    public void init() {

    }

    public void testActivityPosts() throws Exception {
        subscribeActivityTestUtils.addTwoPersonsAsFriends("temkarus0070", "pupkin777");

    }
}
