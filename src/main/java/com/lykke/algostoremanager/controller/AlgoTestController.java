package com.lykke.algostoremanager.controller;

import com.lykke.algostoremanager.api.AlgoContainerManager;
import com.lykke.algostoremanager.exception.AlgoException;
import com.lykke.algostoremanager.exception.AlgoServiceManagerErrorCode;
import com.lykke.algostoremanager.model.Algo;
import com.lykke.algostoremanager.model.AlgoTest;
import com.lykke.algostoremanager.model.AlgoTestStatus;
import com.lykke.algostoremanager.repo.AlgoRepository;
import com.lykke.algostoremanager.repo.AlgoTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by cpt2nmi on 16.10.2017 г..
 */


@RestController
@RequestMapping("/algo/test")

public class AlgoTestController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    AlgoContainerManager algoContainerManager;

    @Autowired
    AlgoRepository algoRepository;

    @Autowired
    AlgoTestRepository algoTestRepository;

    @RequestMapping(value = "/create", method= RequestMethod.PUT)

    public Long testAlgo(@RequestParam Long algoVersion,@RequestParam String name, @RequestParam String appKey){
        Algo algo = algoRepository.findByNameAndVersion(name, algoVersion);
        String containerId = null;
        String containerImageId = algo.getAlgoBuildImageId();

        AlgoTest algoTest = new AlgoTest();
        algoTest.setAlgo(algo);
        algoTest.setStatus(AlgoTestStatus.CREATED.toString());


        if (algo!=null) {
            containerId = algoContainerManager.create(containerImageId, name, appKey);
            algoTest.setContainerId(containerId);
            algoTestRepository.save(algoTest);
        } else {
            throw new AlgoException("Algo not found!!!", AlgoServiceManagerErrorCode.ALGO_NOT_FOUND);
        }
        return algoTest.getId();
    }


    @RequestMapping(value = "/{id}/start", method= RequestMethod.PUT)

    public void start(@PathVariable Long id){
        AlgoTest algotTest = algoTestRepository.findById(id);

        if (algotTest!=null) {

            algoContainerManager.start(algotTest.getContainerId());
            algotTest.setStatus(AlgoTestStatus.RUNNING.toString());
            algoTestRepository.save(algotTest);

        }else{
            throw new AlgoException("AlgoTest not found!!!",AlgoServiceManagerErrorCode.ALGO_TEST_ERROR);

        }


    }

    @RequestMapping(value = "/{id}/pause", method= RequestMethod.PUT)

    public void pauseTestAlgo(@PathVariable Long id){
        AlgoTest algotTest = algoTestRepository.findById(id);

        if (algotTest!=null) {

            algoContainerManager.pause(algotTest.getContainerId());
            algotTest.setStatus(AlgoTestStatus.PAUSED.toString());
            algoTestRepository.save(algotTest);
        }else {
            throw new AlgoException("AlgoTest not found!!!",AlgoServiceManagerErrorCode.ALGO_TEST_ERROR);

        }


    }

    @RequestMapping(value = "/{id}/resume", method= RequestMethod.PUT)

    public void resumeTestAlgo(@PathVariable Long id){
        AlgoTest algotTest = algoTestRepository.findById(id);

        if (algotTest!=null) {

            algoContainerManager.resume(algotTest.getContainerId());
            algotTest.setStatus(AlgoTestStatus.RUNNING.toString());
            algoTestRepository.save(algotTest);

        } else {
            throw new AlgoException("AlgoTest not found!!!",AlgoServiceManagerErrorCode.ALGO_TEST_ERROR);

        }


    }

    @RequestMapping(value = "/{id}/stop", method= RequestMethod.PUT)

    public void stopTestAlgo(@RequestParam Long id){
        AlgoTest algotTest = algoTestRepository.findById(id);

        if (algotTest!=null) {

            algoContainerManager.stop(algotTest.getContainerId());
            algotTest.setStatus(AlgoTestStatus.STOPPED.toString());
            algoTestRepository.save(algotTest);

        } else {
            throw new AlgoException("AlgoTest not found!!!",AlgoServiceManagerErrorCode.ALGO_TEST_ERROR);

        }

    }

    @RequestMapping(value = "/{id}/getLog", method= RequestMethod.GET)

    public String getAlgoLog(@RequestParam Long id){
        AlgoTest algotTest = algoTestRepository.findById(id);

        if (algotTest!=null) {
            return algoContainerManager.getLog(algotTest.getContainerId());
        } else {
            throw new AlgoException("AlgoTest not found!!!",AlgoServiceManagerErrorCode.ALGO_TEST_ERROR);

        }
    }

    @RequestMapping(value = "/{id}/status", method= RequestMethod.GET)

    public String getTestAlgoStatus(@RequestParam Long id){
        AlgoTest algotTest = algoTestRepository.findById(id);

        if (algotTest!=null) {
            return algoContainerManager.getStatus(algotTest.getContainerId());
        }else {
            throw new AlgoException("AlgoTest not found!!!",AlgoServiceManagerErrorCode.ALGO_TEST_ERROR);

        }
    }

    @RequestMapping(value = "/{id}/delete", method= RequestMethod.GET)

    public void deleteTestAlgo(@RequestParam Long id){
        AlgoTest algotTest = algoTestRepository.findById(id);

        if (algotTest!=null) {
             algoContainerManager.delete(algotTest.getContainerId());
             algoTestRepository.delete(id);
        }else {
            throw new AlgoException("AlgoTest not found!!!",AlgoServiceManagerErrorCode.ALGO_TEST_ERROR);

        }
    }



}