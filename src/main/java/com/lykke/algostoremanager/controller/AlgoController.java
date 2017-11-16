package com.lykke.algostoremanager.controller;

import com.lykke.algostoremanager.api.AlgoContainerManager;
import com.lykke.algostoremanager.api.AlgoImageManager;
import com.lykke.algostoremanager.api.AlgoServiceManager;
import com.lykke.algostoremanager.exception.AlgoException;
import com.lykke.algostoremanager.exception.AlgoServiceManagerErrorCode;
import com.lykke.algostoremanager.model.Algo;
import com.lykke.algostoremanager.model.AlgoUser;
import com.lykke.algostoremanager.repo.AlgoRepository;
import com.lykke.algostoremanager.repo.AlgoServiceRepository;
import com.lykke.algostoremanager.repo.AlgoTestRepository;
import com.lykke.algostoremanager.repo.AlgoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by niau on 11/12/17.
 */
@RestController
@RequestMapping("/algo")

public class AlgoController {

    @Autowired
    AlgoServiceManager algoServiceManager;

    @Autowired
    AlgoServiceRepository algoServiceRepository;

    @Autowired
    AlgoImageManager algoImageManager;

    @Autowired
    AlgoUserRepository algoUserRepository;

    @Autowired
    AlgoRepository algoRepository;

    @Autowired
    AlgoTestRepository algoTestRepository;

    @Autowired
    AlgoContainerManager algoContainerManager;



    @RequestMapping(value = "/get", method= RequestMethod.GET)

    public Algo getAlgoLog(@RequestParam String algoUserName,@RequestParam String algoName){

        AlgoUser algoUser = algoUserRepository.findByUserName(algoUserName);

        Algo algo = algoRepository.findByNameAndAlgoUser(algoName,algoUser);

        if (algo!=null) {
            return algo;
        } else {
            throw new AlgoException("Algo not found!!!", AlgoServiceManagerErrorCode.ALGO_NOT_FOUND);

        }
    }
    @RequestMapping(value = "/delete", method= RequestMethod.DELETE)

    public void removeALgo(@RequestParam String algoUserName,@RequestParam String algoName){
        AlgoUser algoUser = algoUserRepository.findByUserName(algoUserName);

        Algo algo = algoRepository.findByNameAndAlgoUser(algoName,algoUser);


        if (algo!=null) {
           algoImageManager.remove(algo.getAlgoBuildImageId());
           algoRepository.delete(algo);

        } else {
            throw new AlgoException("Algo not found!!!", AlgoServiceManagerErrorCode.ALGO_TEST_ERROR);

        }
    }

}