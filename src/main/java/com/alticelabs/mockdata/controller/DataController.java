package com.alticelabs.mockdata.controller;

import com.alticelabs.mockdata.services.DataService;
import com.alticelabs.prototype_common_models.rules.Rule;
import com.alticelabs.prototype_common_models.rules.events.SaveRuleRequest;
import com.alticelabs.prototype_common_models.utils.BillingAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mockdata/data")
public class DataController {

    @Autowired
    private DataService dataService;
    @PostMapping("/accounts")
    public List<BillingAccount> loadAllAccountsToKafka() {
        return dataService.loadAllAccountsToKafka();
    }

    @PostMapping("/rule")
    public SaveRuleRequest loadRuleToKafka(@RequestBody Rule rule) {
        return dataService.loadRuleToKafka(rule);
    }


}
