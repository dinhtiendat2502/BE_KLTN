package com.app.toeic.revai.repo;

import com.app.toeic.revai.model.RevAIAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevAIAccountDTO extends JpaRepository<RevAIAccount, Long>{

}
