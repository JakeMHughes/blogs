package com.hughesportal.dsinsb.repositories;

import com.hughesportal.dsinsb.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillionairesRepository extends JpaRepository<DummyEntity, Integer> {

    //@Query(value = "SELECT JSON_OBJECT('id':1)", nativeQuery = true)

    /*
    SELECT
     CAST(JSON_OBJECT(
        'id':b.id,
        KEY 'firstName' VALUE b.first_name,
        'lastName':b.last_name,
        'career':b.career) as varchar)
    FROM billionaires as b

    SELECT CAST(JSON_ARRAY(
    (SELECT JSON_OBJECT(
        'id':b.id,
       	KEY 'firstName' VALUE b.first_name,
        'lastName':b.last_name,
       	'career':b.career)
    FROM billionaires b)) as varchar)
     */
    @Query(value = "SELECT CAST(JSON_ARRAY( " +
            "    (SELECT JSON_OBJECT( " +
            "       'id'\\:b.id, " +
            "       KEY 'first_name' VALUE b.first_name, " +
            "       'last_name'\\:b.last_name, " +
            "       'career'\\:b.career) " +
            "    FROM billionaires b)) as varchar)", nativeQuery = true)
    String getAllBillionaires();

}


