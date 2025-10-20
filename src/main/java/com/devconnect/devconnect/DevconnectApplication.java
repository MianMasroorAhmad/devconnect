package com.devconnect.devconnect;

import com.devconnect.devconnect.model.*;
import com.devconnect.devconnect.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.devconnect.devconnect.config.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class DevconnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevconnectApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner dataSeeder(
        UserRepository userRepository,
        PostRepository postRepository,
        CommentRepository commentRepository,
        PasswordEncoder passwordEncoder) {

        return args -> {
            // 1️⃣ Create a user
            User user = new User();
            user.setUsername("john_doe");
            user.setEmail("john@example.com");
            user.setPassword(passwordEncoder.encode("secure123"));
            userRepository.save(user);

            // 2️⃣ Create a post for that user
            Post post = new Post();
            post.setContent("Hello, DevConnect!");
            post.setCreatedAt(LocalDateTime.now());
            post.setUser(user); // link to user
            postRepository.save(post);

            // 3️⃣ Add a comment by the same user on the post
            Comment comment = new Comment();
            comment.setText("This is my first comment!");
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUser(user);
            comment.setPost(post);
            commentRepository.save(comment);

            System.out.println("✅ Test data inserted successfully!");
        };
    }
}
