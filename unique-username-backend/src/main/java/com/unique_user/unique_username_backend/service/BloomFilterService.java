package com.unique_user.unique_username_backend.service;

import com.unique_user.unique_username_backend.model.Username;
import com.unique_user.unique_username_backend.repository.UsernameRepository;
import org.springframework.stereotype.Service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class BloomFilterService {

    private BloomFilter<String> bloomFilter;
    private final UsernameRepository usernameRepository;

    public BloomFilterService(UsernameRepository usernameRepository) {
        this.usernameRepository = usernameRepository;
    }

    @PostConstruct
    public void init() {
        // Estimate: 1000 usernames, 1% false positive rate
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),
                                                    1000, 0.01);


        List<Username> usernames = usernameRepository.findAll();
        for(Username user : usernames){
            bloomFilter.put(user.getUsername());
        }

    }

    public boolean mightContain(String username) {
        return bloomFilter.mightContain(username.toLowerCase());
    }

    public void addUsername(String username) {
        bloomFilter.put(username.toLowerCase());
    }
}

