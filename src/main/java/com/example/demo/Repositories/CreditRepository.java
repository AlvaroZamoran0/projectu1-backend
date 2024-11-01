package com.example.demo.Repositories;

import com.example.demo.Entities.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<CreditEntity, Long> {
    public CreditEntity findByIdCredit(Long idCredit);
    public List<CreditEntity> findByDoc(String doc);
    public List<CreditEntity> findByApprovedIsLessThan(Integer approved);
}
