package com.github.axinger.dao;

import com.github.axinger.domain.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDAO extends JpaRepository<BookEntity, Long> {
}
