package it.polimi.ingsw.network.client;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_view.GameViewClient;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.board.Printable;
import it.polimi.ingsw.network.utils.Logger;
import it.polimi.ingsw.observer.Observer;

import static it.polimi.ingsw.game_view.GameViewClient.*;

public class ClientMessageObserverHandler implements Observer<CommunicationMessage> {
    private final GameViewClient view;
    private GameViewClient.InputStateMachine state;
    boolean actionSent = true;

    public ClientMessageObserverHandler(GameViewClient view){
        this.view = view;
    }

    public void updateBoardMessage(GameBoard board){
        Printable.clearScreen();
        view.setBoard(board);
        view.updateBoard(board);
        if(board.isExpertMode()){
            view.displayExpertMode();
        }
        if(board.getCurrentlyPlaying().equals(view.getClient().getName())){
            view.displayYourTurn();
            switch (board.getPhase()){
                case PLANNING_PHASE -> state = GameViewClient.InputStateMachine.PLANNING_PHASE_START;
                case ACTION_PHASE_CHOOSING_CLOUD -> state = GameViewClient.InputStateMachine.CHOOSE_CLOUD_CARD_START;
                case ACTION_PHASE_MOVING_STUDENTS -> state = GameViewClient.InputStateMachine.MOVING_STUDENT_PHASE_START;
                case ACTION_PHASE_MOVING_MOTHER_NATURE -> state = GameViewClient.InputStateMachine.MOVE_MOTHER_NATURE_START;
            }
            actionSent = false;
        } else {
            view.displayOtherPlayerTurn(board.getCurrentlyPlaying());
        }
    }

    @Override
    public void update(CommunicationMessage message) {
        switch (message.getID()){
            case CONNECTION_CONFIRMED -> new Thread(view::askName).start();
            case NAME_MESSAGE -> new Thread(view::reaskName).start();
            case NAME_CONFIRMED, JOINING_ACTION_INFO -> new Thread(view::askJoiningAction).start();
            case JOIN_LOBBY_ACTION_CONFIRMED, LOBBY_TO_JOIN_INFO -> new Thread(() -> view.askLobbyToJoin(message.getMessage())).start();
            case CREATE_LOBBY_ACTION_CONFIRMED, NUMBER_OF_PLAYER_INFO -> new Thread(view::askPlayerNumber).start();
            case NUMBER_OF_PLAYER_CONFIRMED, GAME_TYPE_INFO -> new Thread(view::askGameType).start();
            case LOBBY_JOINED_CONFIRMED -> new Thread(() -> view.displayLobbyJoined(message.getMessage())).start();
            case NO_LOBBIES_AVAILABLE -> new Thread(view::displayNoLobbiesAvailable).start();
            case IS_CHOSING_DECK_TYPE -> new Thread(() -> view.displayIsChoosingDeckType(message.getMessage())).start();
            case ASK_DECK   -> new Thread(() -> view.askDeck(message.getMessage())).start();
            case ASSISTANT_NOT_PLAYABLE -> new Thread(view::reaskAssistant).start();
            case NOT_YOUR_TURN -> new Thread(view::displayNotYourTurn).start();
            case MOVE_STUDENT_FAILED -> new Thread(() -> view.displayErrorMessage(FAILED_TO_MOVE_STUDENT, FAILED_TO_MOVE_STUDENT_ERROR, (GameBoard) message.getMessage())).start();
            case INVALID_MOTHER_NATURE_STEPS -> new Thread(() -> view.displayErrorMessage(FAILED_TO_MOVE_MOTHER_NATURE, FAILED_TO_MOVE_MOTHER_NATURE_ERROR, (GameBoard) message.getMessage())).start();
            case INVALID_CLOUD_CHOSEN -> new Thread(() -> view.displayErrorMessage(INVALID_CLOUD_CHOSEN, INVALID_CLOUD_CHOSEN_ERROR, (GameBoard) message.getMessage())).start();
            case NOT_ACTION_PHASE -> new Thread(() -> view.displayErrorMessage(INVALID_ACTION, INVALID_ACTION_ERROR, (GameBoard) message.getMessage())).start();
            case ADVANCED_NOT_PLAYABLE -> new Thread(() -> view.displayErrorMessage(ADVANCED_CARD_NOT_PLAYABLE, ADVANCED_CARD_NOT_PLAYABLE_ERROR, (GameBoard) message.getMessage())).start();
            case ALREADY_PLAYED_ADVANCED -> new Thread(() -> view.displayErrorMessage(ADVANCED_CARD_ALREADY_PLAYED_IN_TURN, ADVANCED_CARD_ALREADY_PLAYED_IN_TURN_ERROR, (GameBoard) message.getMessage())).start();
            case NOT_EXPERT_GAME -> new Thread(() -> view.displayErrorMessage(NOT_EXPERT_MODE_GAME, NOT_EXPERT_MODE_GAME_ERROR, (GameBoard) message.getMessage())).start();
            case NOT_ENOUGH_MONEY -> new Thread(() -> view.displayErrorMessage(NOT_ENOUGH_MONEY_FOR_ADVANCED_CARD, NOT_ENOUGH_MONEY_FOR_ADVANCED_CARD_ERROR, (GameBoard) message.getMessage())).start();
            case PLAYER_DISCONNECTED -> new Thread(() -> view.onPlayerDisconnection((String) message.getMessage())).start();
            case INFO -> Logger.INFO((String) message.getMessage());
            case ERROR -> Logger.ERROR((String) message.getMessage(), "General Error");
            case GAME_READY -> new Thread(() -> view.gameReady((GameBoard) message.getMessage())).start();
            case VIEW_UPDATE -> new Thread(() -> updateBoardMessage((GameBoard) message.getMessage())).start();
            case YOU_WIN -> new Thread(() -> { updateBoardMessage((GameBoard) message.getMessage()); view.displayEndGame(CommunicationMessage.MessageType.YOU_WIN); }).start();
            case YOU_LOSE -> new Thread(() -> { updateBoardMessage((GameBoard) message.getMessage()); view.displayEndGame(CommunicationMessage.MessageType.YOU_LOSE); }).start();
            case DRAW -> new Thread(() -> { updateBoardMessage((GameBoard) message.getMessage()); view.displayEndGame(CommunicationMessage.MessageType.DRAW); }).start();
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
