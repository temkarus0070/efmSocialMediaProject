package org.temkarus0070.efmsocialmedia.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.temkarus0070.efmsocialmedia.dto.PostDto;
import org.temkarus0070.efmsocialmedia.entities.Image;
import org.temkarus0070.efmsocialmedia.entities.Post;
import org.temkarus0070.efmsocialmedia.repositories.ImageRepository;
import org.temkarus0070.efmsocialmedia.repositories.PostRepository;
import org.temkarus0070.efmsocialmedia.utils.PostUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private PostRepository postRepository;
    private ImageRepository imageRepository;


    public PostDto create(PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        postDto.setAuthorName(authentication.getName());
        Post savedPost = postRepository.save(PostUtils.mapToPost(postDto));
        postDto.setId(savedPost.getId());
        postDto.setCreated(savedPost.getCreated());
        return postDto;
    }

    @PostAuthorize("returnObject.author.username == authentication.name")
    public Post edit(PostDto updatedPost) {
        Optional<Post> postToEditOptional = postRepository.findById(updatedPost.getId());

        if (postToEditOptional.isPresent()) {
            Post postToEdit = postToEditOptional.get();
            postToEdit.setText(updatedPost.getText());
            postToEdit.setHeader(updatedPost.getHeader());
            return postRepository.save(postToEdit);
        }
        throw new EntityNotFoundException("Не найден пост для редактирования");
    }

    @PostAuthorize("returnObject.author.username == authentication.name")
    public Post addImage(long postId, byte[] image) {
        Optional<Post> postToEditOptional = postRepository.findById(postId);

        if (postToEditOptional.isPresent()) {
            Post postToEdit = postToEditOptional.get();
            Image savedImage = imageRepository.save(new Image(0, image, postToEdit));
            postToEdit.setImages(List.of(savedImage));
            return postToEdit;
        }
        throw new EntityNotFoundException("Не найден пост для добавления изображения");

    }

    public PostDto get(long postId) {
        Optional<Post> optionalPost = postRepository.findByIdEagerly(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            return PostUtils.mapFromPost(post);
        }
        throw new EntityNotFoundException("Пост не найден");
    }

    @PostAuthorize("returnObject.author.username == authentication.name")
    public Post remove(long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            postRepository.deleteById(postId);
            return optionalPost.get();
        }
        throw new EntityNotFoundException("Пост не найден");
    }

    @PostAuthorize("returnObject.author.username == authentication.name")
    public Post removeImage(long postId, long imageId) {
        Optional<Post> postToEditOptional = postRepository.findById(postId);

        if (postToEditOptional.isPresent()) {
            imageRepository.deleteById(imageId);
            return postToEditOptional.get();
        }
        throw new EntityNotFoundException("Не найден пост для удаления изображения");

    }
}
