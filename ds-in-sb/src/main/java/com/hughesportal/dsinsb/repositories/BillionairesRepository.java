package com.hughesportal.dsinsb.repositories;

import com.hughesportal.dsinsb.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;

@Repository
public interface BillionairesRepository extends JpaRepository<DummyEntity, Integer> {

    @Query(value = "SELECT CAST(JSON_ARRAY( " +
            "    (SELECT JSON_OBJECT( " +
            "       'id'\\:b.id, " +
            "       KEY 'first_name' VALUE b.first_name, " +
            "       'last_name'\\:b.last_name, " +
            "       'career'\\:b.career) " +
            "    FROM billionaires b)) as varchar)", nativeQuery = true)
    String getAllBillionaires();

    @Modifying
    @Query( value = "INSERT INTO billionaires (first_name, last_name, career) VALUES (:firstName, :lastName, :career)", nativeQuery = true)
    @Transactional
    int insertBillionaire(@Param("firstName") String firstName,@Param("lastName") String lastName,@Param("career") String career);

    @Query(value = "CALL IDENTITY()", nativeQuery = true)
    int getLastInsert();

    @Query(value = "SELECT CAST( " +
            "    (SELECT JSON_OBJECT( " +
            "       'id'\\:b.id, " +
            "       KEY 'first_name' VALUE b.first_name, " +
            "       'last_name'\\:b.last_name, " +
            "       'career'\\:b.career) " +
            "    FROM billionaires b WHERE b.id = :id ) as varchar)", nativeQuery = true)
    String getBillionaireByID(@Param("id") int id);

    @Modifying
    @Query(value = "UPDATE billionaires " +
                    "SET first_name = :firstName , last_name = :lastName , career = :career " +
                    "WHERE id = :id ", nativeQuery = true)
    @Transactional
    int updateBillionaireByID(@Param("id") int id, @Param("firstName") String firstName,@Param("lastName") String lastName,@Param("career") String career);

    @Modifying
    @Query(value = "DELETE FROM billionaires WHERE id = :id",nativeQuery = true)
    @Transactional
    int deleteBillionaireByID(@Param("id") int id);

}


