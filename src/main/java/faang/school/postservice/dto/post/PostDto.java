package faang.school.postservice.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.postservice.dto.CommentDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {
    private Long id;
    @NotBlank(message = "Content is required")
    private String content;
    private Long authorId;
    private Long projectId;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
    private boolean published;
    private boolean deleted;
    private Long likeCount;
}