package com.noetic.subscriptiongatewaysecurity.service;

import com.noetic.subscriptiongatewaysecurity.entities.Client;
import com.noetic.subscriptiongatewaysecurity.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc service class to manage all the configurations
 */
@Service
public class ConfigurationDataManagerService {

    @Autowired
    private ClientRepository planAccountRepo;
    Map<String, Client> planAccountEnitiy = new HashMap<>();

    /***
     * @desc get All data from vendorPlanAccount table and put it into Map wiht serviceid Key
     */
    public void loadVendorPlanAccounts() {
        List<Client> all = planAccountRepo.findAll();
        all.forEach(vendorPlanAccount -> planAccountEnitiy.put(vendorPlanAccount.getClientId(), vendorPlanAccount));
    }

    /**
     * @desc load all configurations
     */
    public void bootstapAndCacheConfigurationData() {
        loadVendorPlanAccounts();
    }

    /**
     * @desc will get plan account from map using cliendId
      * @param cliendId
     * @return
     */
    public Client getVendorPlanAccount(String cliendId){
        return planAccountEnitiy.get(cliendId);
    }

}
