package com.ll.exam.app__2022_10_05.app.article.repository;

import com.ll.exam.app__2022_10_05.app.member.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}