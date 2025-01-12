package com.edgerank.demo.Service;


import com.edgerank.demo.Entity.Post;
import com.edgerank.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EdgeRank {
    @Autowired
    private PostRepository postRepository;

    private static final double POIDS_REACTION = 0.1;
    private static final double POIDS_COMMENTAIRE = 7.0;
    private static final double POIDS_PARTAGE = 3.0;
    private static final double FACTEUR_DECROISSANCE_TEMPS = 0.8;

    public List<Post> getPersonalizedFeed(Long userId) {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .sorted((p1,p2) -> Double.compare(calculateEdgeRank(p2), calculateEdgeRank(p1)))
                .collect(Collectors.toList());
    }

    private double calculateEdgeRank(Post post) {
        double baseScore = (POIDS_REACTION * post.getReactions()) + (POIDS_COMMENTAIRE * post.getComments()) + (POIDS_PARTAGE * post.getShares());
        double timeDecay = Math.pow(FACTEUR_DECROISSANCE_TEMPS, Duration.between(post.getTimestamp(), Instant.now()).toDays());

        return baseScore * timeDecay;
    }

}
