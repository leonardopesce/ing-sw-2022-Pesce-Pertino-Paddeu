package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.advanced.*;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.Island;

public class AdvancedCardController {

    /**
     *  card is given by controller which has access to the game
     * @param card
     * @param args
     */
    public void playEffectOfCard(AdvancedCharacter card, Object... args ){
        switch (card.getType()){
            case MONK -> ((Monk)card).playEffect((Island)args[0], (Integer)args[1]);
            case BARD -> ((Bard)card).playEffect();
            case MERCHANT -> ((Merchant)card).playEffect((ColorCharacter) args[0]);
            case FLAGMAN -> ((Flagman)card).playEffect();
            case POSTMAN -> ((Postman)card).playEffect();
            case PRINCESS -> ((Princess)card).playEffect();
            case CENTAURUS -> ((Centaurus)card).playEffect();
            case KNIGHT -> ((Knight)card).playEffect();
            case HEALER -> ((Healer)card).playEffect((Island) args[0]);
            case BARTENDER -> ((Bartender)card).playEffect();
            case JESTER -> ((Jester)card).playEffect();
            case LANDLORD -> ((Landlord)card).playEffect();
        }
    }

    private boolean checkArgument(AdvancedCharacter card, Object... args){
        //TODO check if argument are correct
        return switch (card.getType()){
            case MONK -> (args[0] instanceof Island) & (args[1] instanceof Integer) & args.length == 2;
            case BARD -> args.length == 0;
            case MERCHANT -> args.length == 0;
            case FLAGMAN -> args.length == 0;
            case POSTMAN -> args.length == 0;
            case PRINCESS -> args.length == 0;
            case CENTAURUS -> args.length == 0;
            case KNIGHT -> args.length == 0;
            case HEALER -> args.length == 0;
            case BARTENDER -> args.length == 0;
            case JESTER -> args.length == 0;
            case LANDLORD -> args.length == 0;
            default -> false;
        };
    }
}
