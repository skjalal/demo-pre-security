package com.example.config;

import com.example.domain.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@RequiredArgsConstructor
public class ApiPermissionEvaluator extends DenyAllPermissionEvaluator {

  private final EntityManager entityManager;
  private final TransactionTemplate transactionTemplate;

  @Override
  public boolean hasPermission(
      Authentication authentication, Object targetType, Object permission) {
    return hasPrivilege(authentication, targetType, permission);
  }

  private boolean hasPrivilege(Authentication auth, Object targetType, Object permission) {
    log.info("Enter into API");
    if (auth == null || targetType == null || !(permission instanceof List<?> list)) {
      return false;
    }
    list.removeIf(Objects::isNull);
    var partnerId = list.get(0).toString();
    log.debug("PartnerId: {}", partnerId);
    var name = list.get(1).toString();
    try {
      Optional.ofNullable(transactionTemplate.execute(d -> process(d, name)))
          .orElseGet(List::of)
          .forEach(this::print);
    } catch (Exception e) {
      log.error("Failed to fetch record", e);
    }
    return true;
  }

  private List<User> process(TransactionStatus status, String name) {
    log.debug("Status: {}", status);
    var query =
        entityManager.createQuery(
            "SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name,'%'))",
            User.class);
    query.setParameter("name", name);
    return query.getResultList();
  }

  private void print(User user) {
    log.info("Id: {}", user.getId());
  }
}
