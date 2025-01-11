package com.edgerank.demo.Entity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // reaction, comment, share
    private Instant timestamp = Instant.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
