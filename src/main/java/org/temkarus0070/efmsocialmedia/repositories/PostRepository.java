package org.temkarus0070.efmsocialmedia.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.entities.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph("post.images")
    @Query("SELECT  p from Post p where p.id=:id")
    Optional<Post> findByIdEagerly(@Param("id") Long id);

    public Page<Post> findAllByAuthor_UsernameIn(List<String> usernames, PageRequest pageable);
}
