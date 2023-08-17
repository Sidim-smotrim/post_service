package faang.school.postservice.service.postCorrecter;

import faang.school.postservice.client.BingSpellClient;
import faang.school.postservice.dto.postCorrecter.FlaggedTokenDto;
import faang.school.postservice.dto.postCorrecter.SpellCheckDto;
import faang.school.postservice.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class PostCorrecter {
    @Value("${ai-spelling.mode}")
    private String mode;
    private final BingSpellClient bingSpellClient;

    @Async("bingSpellAsyncExecutor")
    @Retryable(maxAttempts = 10, backoff = @Backoff(delay = 1000))
    public CompletableFuture<String> correctPostText(Post post) {
        String body = "Text=" + post.getContent();
        SpellCheckDto spellCheckDto = bingSpellClient.checkSpell(mode, body).getBody();

        if (spellCheckDto == null) {
            return CompletableFuture.completedFuture(post.getContent());
        }

        List<FlaggedTokenDto> flaggedTokenList = spellCheckDto.getFlaggedTokens();
        String text = post.getContent();
        for (FlaggedTokenDto flaggedToken : flaggedTokenList) {
            String token = flaggedToken.getToken();
            String correct = flaggedToken.getSuggestions().get(0).getSuggestion();
            text = text.replace(token, correct);
        }

        return CompletableFuture.completedFuture(text);
    }
}
