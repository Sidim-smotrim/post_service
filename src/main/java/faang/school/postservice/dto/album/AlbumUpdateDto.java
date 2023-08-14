package faang.school.postservice.dto.album;

import faang.school.postservice.model.album.AlbumVisibility;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumUpdateDto {
    private String title;
    private String description;
    @NotNull
    private Long authorId;
    private List<Long> postsId;
    private AlbumVisibility visibility;
    private List<Long> usersWithAccessIds;
    private LocalDateTime updatedAt;
}
