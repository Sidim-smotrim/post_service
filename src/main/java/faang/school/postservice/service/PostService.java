package faang.school.postservice.service;

import faang.school.postservice.model.Post;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.util.ModerationDictionary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ModerationDictionary moderationDictionary;
    private final Integer batchSize;

    public PostService(PostRepository postRepository, ModerationDictionary moderationDictionary, @Value("${post.moderator.scheduler.batchSize}") Integer batchSize) {
        this.postRepository = postRepository;
        this.moderationDictionary = moderationDictionary;
        this.batchSize = batchSize;
    }

    @Transactional
    public void verifyContent() {
        List<Post> posts = postRepository.findAllByVerifiedAtIsNull();
        List<List<Post>> grouped = new ArrayList<>();
        if (posts.size() > batchSize) {
            int i = 0;
            while (i < posts.size() / batchSize) {
                grouped.add(posts.subList(i, i + batchSize));
                i += batchSize;
            }
            if (i < posts.size()) {
                grouped.add(posts.subList(i, posts.size()));
            }
        } else {
            grouped.add(posts);
        }

        List<CompletableFuture<Void>> completableFutures = grouped.stream()
                .map(list -> CompletableFuture.runAsync(() -> verifySublist(list)))
                .toList();

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
    }

    private void verifySublist(List<Post> subList) {
        subList.forEach(post -> {
            post.setVerified(!moderationDictionary.containsBadWord(post.getContent()));
            post.setVerifiedAt(LocalDateTime.now());
        });
        postRepository.saveAll(subList);
    }
}
