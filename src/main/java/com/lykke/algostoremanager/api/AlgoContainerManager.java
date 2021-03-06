package com.lykke.algostoremanager.api;

/**
 * Created by niau on 10/23/17.
 */
public interface AlgoContainerManager {
    String create(String imageId,String name);
    String getLog(String id);
    String getLog(String id,int tail);
    void stop(String id);
    void start(String id);
    void pause(String id);
    void resume(String id);

    void delete(String id);
    String getStatus(String id);


}