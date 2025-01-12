package com.edgerank.demo.Controller;


import com.edgerank.demo.Entity.Post;
import com.edgerank.demo.Entity.PostService;
import com.edgerank.demo.Service.EdgeRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EdgeRankController {

    @Autowired
    private PostService postService;

    @Autowired
    private EdgeRank edgeRank;

    @GetMapping("/feed")
    public List<Post> getPersonalizedFeed(@RequestParam Long userId) {
        return edgeRank.getPersonalizedFeed(userId);
    }


    @GetMapping("/post")
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }


}
