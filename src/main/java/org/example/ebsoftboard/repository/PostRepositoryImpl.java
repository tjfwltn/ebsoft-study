package org.example.ebsoftboard.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.ebsoftboard.dto.PostResponseDTO;
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
    private EntityManager em;

    @Override
    public Page<PostResponseDTO> searchPosts(PostSearchCondition condition, Pageable pageable) {
        String base = "FROM Post p LEFT JOIN p.category c WHERE 1=1";
        StringBuilder where = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (condition.getCategoryId() != null) {
            where.append(" AND c.id = :categoryId");
            params.put("categoryId", condition.getCategoryId());
        }

        if (condition.getKeyword() != null && !condition.getKeyword().isEmpty()) {
            where.append(" AND (p.title LIKE :keyword OR p.username LIKE :keyword OR p.contents LIKE :keyword)");
            params.put("keyword", "%" + condition.getKeyword() + "%");
        }

        where.append(" AND p.createdAt BETWEEN :startDate AND :endDate");
        params.put("startDate", condition.getStartDateOrDefault());
        params.put("endDate", condition.getEndDateOrDefault());

        String jpql = """
                SELECT new org.example.ebsoftboard.dto.PostResponseDTO(
                p.id,
                p.title,
                c.id,
                c.title,
                p.username,
                p.viewCount,
                p.createdAt,
                p.modifiedAt,
                (SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM File f WHERE f.post.id = p.id))
                """ + base + where;

        String countJpql = "SELECT COUNT(p) " + base + where;

        List<PostResponseDTO> content = em.createQuery(jpql, PostResponseDTO.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .setParameter("startDate", params.get("startDate"))
                .setParameter("endDate", params.get("endDate"))
                .setParameter("categoryId", params.get("categoryId"))
                .setParameter("keyword", params.get("keyword"))
                .getResultList();

        Long total = em.createQuery(countJpql, Long.class)
                .setParameter("startDate", params.get("startDate"))
                .setParameter("endDate", params.get("endDate"))
                .setParameter("categoryId", params.get("categoryId"))
                .setParameter("keyword", params.get("keyword"))
                .getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}
