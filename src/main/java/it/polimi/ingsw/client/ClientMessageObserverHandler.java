package it.polimi.ingsw.client;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_view.GameViewClient;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.observer.Observer;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PONG;

public class ClientMessageObserverHandler implements Observer<CommunicationMessage> {
    private final GameViewClient view;
    private GameViewClient.InputStateMachine state;
    boolean actionSent = true;

    public ClientMessageObserverHandler(GameViewClient view){
        this.view = view;

    }

    public void updateBoardMessage(GameBoard board){
        view.setBoard(board);
        if(board.getCurrentlyPlaying().equals(view.getClient().getName())){
            view.updateBoard(board);
            view.displayYourTurn();
            if(board.isExpertMode()){
                view.displayExpertMode();
            }
            switch (board.getPhase()){
                case PLANNING_PHASE -> state = GameViewClient.InputStateMachine.PLANNING_PHASE_START;
                case ACTION_PHASE_CHOOSING_CLOUD -> state = GameViewClient.InputStateMachine.CHOOSE_CLOUD_CARD_START;
                case ACTION_PHASE_MOVING_STUDENTS -> state = GameViewClient.InputStateMachine.MOVING_STUDENT_PHASE_START;
                case ACTION_PHASE_MOVING_MOTHER_NATURE -> state = GameViewClient.InputStateMachine.MOVE_MOTHER_NATURE_START;
            }
            actionSent = false;
        }
    }

    @Override
    public void update(CommunicationMessage message) {
        switch (message.getID()){
            case ASK_NAME   -> view.askName();
            case REASK_NAME -> view.reaskName();
            case ASK_DECK   -> view.askDeck(message.getMessage());
            case ASK_GAME_TYPE -> view.askGameType();
            case ASK_JOINING_ACTION -> view.askJoiningAction();
            case ASK_LOBBY_TO_JOIN -> view.askLobbyToJoin(message.getMessage());
            case ASK_PLAYER_NUMBER -> view.askPlayerNumber();
            case ASSISTANT_NOT_PLAYABLE -> view.reaskAssistant();
            case ERROR -> System.out.println(message.getMessage());
            case GAME_READY -> view.gameReady((GameBoard) message.getMessage());
            case VIEW_UPDATE -> updateBoardMessage((GameBoard) message.getMessage());
            case YOU_WIN -> System.out.println("YOU WIN!!!");
            case YOU_LOSE -> System.out.println("YOU LOSE!");
        }
    }

    public GameViewClient.InputStateMachine getState() {
        return state;
    }

    public void setState(GameViewClient.InputStateMachine state) {
        this.state = state;
    }

    public boolean isActionSent() {
        return actionSent;
    }

    public void setActionSent(boolean actionSent) {
        this.actionSent = actionSent;
    }
}
