package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.advanced.*;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.Island;

import java.util.List;

public class AdvancedCardController {


    /**
     *  card is given by controller which has access to the game
     * @param card
     * @param args
     */
    public static void playEffectOfCard(AdvancedCharacter card, Object... args ){
        switch (card.getType()){
            case MONK -> ((Monk)card).playEffect((Island)args[0], (Integer)args[1]);
            case BARD -> ((Bard)card).playEffect((Player)args[0], (List<Integer>)args[1], (List<ColorCharacter>)args[2]);
            case MERCHANT -> ((Merchant)card).playEffect((ColorCharacter) args[0]);
            case FLAGMAN -> ((Flagman)card).playEffect((Integer)args[0]);
            case POSTMAN -> ((Postman)card).playEffect((Player)args[0]);
            case PRINCESS -> ((Princess)card).playEffect((Player)args[0], (Integer)args[1]);
            case CENTAURUS -> ((Centaurus)card).playEffect();
            case KNIGHT -> ((Knight)card).playEffect();
            case HEALER -> ((Healer)card).playEffect((Island) args[0]);
            case BARTENDER -> ((Bartender)card).playEffect();
            case JESTER -> ((Jester)card).playEffect((Player)args[0], (List<Integer>)args[1], (List<Integer>)args[2]);
            case LANDLORD -> ((Landlord)card).playEffect((ColorCharacter)args[0]);
        }
    }

    public static boolean checkArgument(AdvancedCharacter card, Object... args){
        //TODO check if argument are correct
        return switch (card.getType()){
            case MONK -> (args[0] instanceof Island) & (args[1] instanceof Integer) & args.length == 2;
            case BARD -> args.length == 3;
            case MERCHANT -> args.length == 1;
            case FLAGMAN -> args.length == 1;
            case POSTMAN -> args.length == 1;
            case PRINCESS -> args.length == 2;
            case CENTAURUS -> args.length == 0;
            case KNIGHT -> args.length == 0;
            case HEALER -> args.length == 1;
            case BARTENDER -> args.length == 0;
            case JESTER -> args.length == 3;
            case LANDLORD -> args.length == 1;
            default -> false;
        };
    }
}
