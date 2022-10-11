package com.ll.exam.app__2022_10_05.app.article.service;

import com.ll.exam.app__2022_10_05.app.article.repository.ArticleRepository;
import com.ll.exam.app__2022_10_05.app.member.entity.Article;
import com.ll.exam.app__2022_10_05.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article write(Member author, String subject, String content) {
        Article article = Article.builder()
                .author(author)
                .subject(subject)
                .content(content)
                .build();

        articleRepository.save(article);

        return article;
    }
}