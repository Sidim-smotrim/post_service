package faang.school.postservice.repository.redis;

import faang.school.postservice.dto.hash.AuthorHash;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public abstract class AuthorRedisRepository implements CrudRepository<AuthorHash, Long> {

    private final RedisTemplate<Long, AuthorHash> redisTemplate;

    public void saveInRedis(AuthorHash authorHash) {
        redisTemplate.opsForZSet().add(
                authorHash.getEventId(), authorHash, authorHash.getEventId());
    }
}
