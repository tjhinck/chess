package service;

import dataaccess.GameDao;

public class CreateGameService {
    private final GameDao gameDao;

    public CreateGameService(GameDao gameDao){
        this.gameDao = gameDao;
    }


}
