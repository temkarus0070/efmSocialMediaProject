package org.temkarus0070.efmsocialmedia.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.temkarus0070.efmsocialmedia.entities.Image;
import org.temkarus0070.efmsocialmedia.entities.Post;
import org.temkarus0070.efmsocialmedia.repositories.ImageRepository;
import org.temkarus0070.efmsocialmedia.repositories.PostRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
    private PostRepository postRepository;
    private ImageRepository imageRepository;


    public Post create(Post post) {
        return postRepository.save(post);
    }

    @PostAuthorize("")
    public Post edit(Post updatedPost) {
        Optional<Post> postToEditOptional = postRepository.findById(updatedPost.getId());

        if (postToEditOptional.isPresent()) {
            Post postToEdit = postToEditOptional.get();
            postToEdit.setText(updatedPost.getText());
            postToEdit.setHeader(updatedPost.getHeader());
        }
        throw new EntityNotFoundException("Не найден пост для редактирования");
    }

    @PostAuthorize("")
    public Post addImage(long postId, byte[] image) {
        Optional<Post> postToEditOptional = postRepository.findById(postId);

        if (postToEditOptional.isPresent()) {
            Post postToEdit = postToEditOptional.get();
            imageRepository.save(new Image(0, image, postToEdit));
        }
        throw new EntityNotFoundException("Не найден пост для добавления изображения");

    }

    public Post get(long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            return optionalPost.get();
        }
        throw new EntityNotFoundException("Пост не найден");
    }

    @PostAuthorize("")
    public Post remove(long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            postRepository.deleteById(postId);
            return optionalPost.get();
        }
        throw new EntityNotFoundException("Пост не найден");
    }
}
