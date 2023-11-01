package com.example.Shareit.Item;
//Pull requests
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByItem_Id(int ItemId);
}
