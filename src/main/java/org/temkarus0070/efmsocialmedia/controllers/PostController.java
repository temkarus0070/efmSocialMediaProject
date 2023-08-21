package org.temkarus0070.efmsocialmedia.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.dto.PostDto;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;
import org.temkarus0070.efmsocialmedia.services.PostService;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
@Tag(name = "Post API", description = "Работа с постами")
public class PostController {

    private PostService postService;

    @GetMapping("/{postId}")
    @Operation(description = "получить пост по идентификатору", responses = {
        @ApiResponse(description = "пост не найден", responseCode = "404",
                     content = @Content(schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(description = "пост найден", responseCode = "200")})
    public PostDto get(@PathVariable long postId) {
        return postService.get(postId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "создать новый пост", responses = @ApiResponse(
        content = @Content(schema = @Schema(description = "id добавленного поста", type = "integer", format = "int64")),
        responseCode = "201"))
    public long create(@Valid @RequestBody PostDto post) {
        return postService.create(post)
                          .getId();
    }

    @PatchMapping
    @Operation(description = "отредактировать пост", responses = {
        @ApiResponse(description = "Нет прав для редактирования поста", responseCode = "403",
                     content = @Content(schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(description = "Пост отредактирован", responseCode = "200")})
    public void edit(@Valid @RequestBody PostDto post) {
        postService.edit(post);
    }

    @PostMapping("/{postId}/image")
    @Operation(description = "добавить изображение к посту", responses = {
        @ApiResponse(description = "Нет прав для  добавления изображения к посту", responseCode = "403",
                     content = @Content(schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(description = "Картинка добавлена", responseCode = "201", content = @Content(
            schema = @Schema(format = "int64", type = "integer", title = "id добавленного изображения")))})
    @ResponseStatus(HttpStatus.CREATED)
    public long addImage(@PathVariable long postId,
                         @NotNull(message = "изображение не может быть null") @RequestBody byte[] image) {
        return postService.addImage(postId, image)
                          .getImages()
                          .stream()
                          .findAny()
                          .get()
                          .getId();
    }

    @DeleteMapping("/{postId}/image/{imageId}")
    @Operation(description = "удалить изображение", responses = {
        @ApiResponse(description = "Нет прав для  удаления изображения", responseCode = "403",
                     content = @Content(schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(description = "Картинка удалена", responseCode = "200")})
    public void removeImage(@PathVariable long postId, @PathVariable long imageId) {
        postService.removeImage(postId, imageId);
    }


    @DeleteMapping("/{postId}")
    @Operation(description = "удалить пост", responses = {
        @ApiResponse(description = "Нет прав для  удаления поста", responseCode = "403",
                     content = @Content(schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(description = "Пост удален", responseCode = "200")})
    public void remove(@PathVariable long postId) {
        postService.remove(postId);
    }

}
