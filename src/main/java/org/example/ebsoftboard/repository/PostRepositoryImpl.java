package org.example.ebsoftboard.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.example.ebsoftboard.dto.PostListResponseDTO;
import org.example.ebsoftboard.dto.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Page<PostListResponseDTO> searchPosts(PostSearchCondition condition, Pageable pageable) {
        StringBuilder selectJpql = new StringBuilder("""
        SELECT new org.example.ebsoftboard.dto.PostListResponseDTO(
            p.id,
            p.title,
            c.title,
            p.username,
            p.viewCount,
            p.createdAt,
            p.modifiedAt,
            (SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM File f WHERE f.post.id = p.id))
        FROM Post p
        LEFT JOIN p.category c
        WHERE 1=1
    """);

        StringBuilder countJpql = new StringBuilder("""
        SELECT COUNT(p)
        FROM Post p
        LEFT JOIN p.category c
        WHERE 1=1
    """);

        Map<String, Object> params = new HashMap<>();
        applyConditions(condition, selectJpql, countJpql, params);

        selectJpql.append(" ORDER BY p.createdAt DESC");

        TypedQuery<PostListResponseDTO> contentQuery = em.createQuery(selectJpql.toString(), PostListResponseDTO.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        TypedQuery<Long> countQuery = em.createQuery(countJpql.toString(), Long.class);

        setQueryParameters(params, contentQuery, countQuery);

        List<PostListResponseDTO> content = contentQuery.getResultList();
        Long total = countQuery.getSingleResult();
        return new PageImpl<>(content, pageable, total);
    }

    private static void applyConditions(PostSearchCondition condition, StringBuilder selectJpql, StringBuilder countJpql, Map<String, Object> params) {
        if (condition.getCategoryId() != null) {
            String clause = " AND c.id = :categoryId";
            selectJpql.append(clause);
            countJpql.append(clause);
            params.put("categoryId", condition.getCategoryId());
        }

        if (condition.getKeyword() != null && !condition.getKeyword().isBlank()) {
            String clause = """
            AND (p.title LIKE :keyword OR p.username LIKE :keyword OR p.contents LIKE :keyword)
        """;
            selectJpql.append(clause);
            countJpql.append(clause);
            params.put("keyword", "%" + condition.getKeyword() + "%");
        }

        String dateClause = " AND p.createdAt BETWEEN :startDate AND :endDate";
        selectJpql.append(dateClause);
        countJpql.append(dateClause);
        params.put("startDate", condition.getStartDateOrDefault());
        params.put("endDate", condition.getEndDateOrDefault());
    }

    private static void setQueryParameters(Map<String, Object> params, TypedQuery<PostListResponseDTO> contentQuery, TypedQuery<Long> countQuery) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            contentQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
