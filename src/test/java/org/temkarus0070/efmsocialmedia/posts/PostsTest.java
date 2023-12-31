package org.temkarus0070.efmsocialmedia.posts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.temkarus0070.efmsocialmedia.dto.PostDto;

public class PostsTest extends BasePostTest {

    @Autowired
    private PostUtils postUtils;

    @Test
    @WithMockUser("temkarus0070")
    public void testPostCreate() throws Exception {
        PostDto postDto = postUtils.createSomePost();
        postUtils.checkPostSuccessCreatedAndAddedToApp(postDto);
    }

    @Test
    @WithMockUser("temkarus0070")
    public void testPostRemove() throws Exception {
        PostDto postDto = postUtils.createSomePost();
        long postId = postUtils.checkPostSuccessCreatedAndAddedToApp(postDto);
        postUtils.checkPostSuccessRemoved(postId);
    }

    @Test
    @WithMockUser("temkarus0070")
    public void testPostEdit() throws Exception {

        PostDto postDto = postUtils.createSomePost();
        long postId = postUtils.checkPostSuccessCreatedAndAddedToApp(postDto);
        postDto.setId(postId);
        postUtils.checkPostSuccessEdited(postDto);

    }


    @Test
    @WithMockUser("temkarus0070")
    public void testImageAdding() throws Exception {

        PostDto postDto = postUtils.createSomePost();
        long postId = postUtils.checkPostSuccessCreatedAndAddedToApp(postDto);
        postDto.setId(postId);
        postUtils.checkImageSuccessfulAdded(postDto);

    }

    @Test
    @WithMockUser("temkarus0070")
    public void testImageRemoving() throws Exception {

        PostDto postDto = postUtils.createSomePost();
        long postId = postUtils.checkPostSuccessCreatedAndAddedToApp(postDto);
        postDto.setId(postId);
        long imageId = postUtils.checkImageSuccessfulAdded(postDto);
        postUtils.checkImageSuccessfulRemoved(imageId, postId);
    }

}
