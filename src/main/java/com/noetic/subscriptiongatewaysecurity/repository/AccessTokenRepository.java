/*
package com.noetic.subscriptiongatewaysecurity.repository;

import com.noetic.subscriptiongatewaysecurity.entities.VendorAccountAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public
interface AccessTokenRepository extends JpaRepository<VendorAccountAccessEntity,Integer> {

    VendorAccountAccessEntity findByAccessToken(String accessToken);
    VendorAccountAccessEntity findAccessTokenByVendorPlanId(Integer planId);
    List<VendorAccountAccessEntity> findByVendorPlanId(Integer planId);
    @Query(value = "select * from vendor_account_access where vendor_plan_id=:plandId",nativeQuery = true)
    VendorAccountAccessEntity test(@Param("plandId") Integer planId);
    void deleteByAccessToken(String accessToken);

   // Optional<VpAccountAccess> findByRefreshToken(String refreshToken);

  //  Optional<VpAccountAccess> findByAuthenticationId(String authenticationId);
}
*/
