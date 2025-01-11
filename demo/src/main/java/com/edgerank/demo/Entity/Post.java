package com.edgerank.demo.Entity;


import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Instant timestamp = Instant.now();
    private int reaction = 0;
    private int comments = 0;
    private int shares = 0;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}
