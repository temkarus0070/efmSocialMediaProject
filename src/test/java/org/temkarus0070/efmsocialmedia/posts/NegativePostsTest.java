package org.temkarus0070.efmsocialmedia.posts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.temkarus0070.efmsocialmedia.BaseApiTest;
import org.temkarus0070.efmsocialmedia.dto.PostDto;

public class NegativePostsTest extends BaseApiTest {

    @Autowired
    private PostUtils postUtils;

    @Test
    @WithMockUser("temkarus0070")
    public void testPostEditWhenNoRights() throws Exception {

        PostDto postDto = postUtils.createSomePost();
        long postId = postUtils.checkPostSuccessCreatedAndAddedToApp(postDto);
        postDto.setId(postId);
        postUtils.checkPostNotSuccessEditedByNotAuthor(postDto, "pupkin777");

    }

    @Test
    @WithMockUser("temkarus0070")
    public void testImageAddWhenNoRights() throws Exception {

        PostDto postDto = postUtils.createSomePost();
        long postId = postUtils.checkPostSuccessCreatedAndAddedToApp(postDto);
        postDto.setId(postId);
        postUtils.checkImageCantBeAddedByNotAuthor(postDto, "pupkin777");

    }

    @Test
    @WithMockUser("temkarus0070")
    public void testImageRemoveWhenNoRights() throws Exception {

        PostDto postDto = postUtils.createSomePost();
        long postId = postUtils.checkPostSuccessCreatedAndAddedToApp(postDto);
        postDto.setId(postId);
        postUtils.checkImageCantBeRemovedByNotAuthor(postDto, "pupkin777");

    }
}
