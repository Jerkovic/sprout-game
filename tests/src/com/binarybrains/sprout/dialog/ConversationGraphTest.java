package com.binarybrains.sprout.dialog;

import com.badlogic.gdx.math.MathUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;

public class ConversationGraphTest {

    static Hashtable<String, Conversation> _conversations;
    static ConversationGraph _graph;

    static String quit = "q";
    static String _input = "";


    @Before
    public void setUp() throws Exception {
        _conversations = new Hashtable<>();

        Conversation start = new Conversation();
        start.setId("500");
        start.setDialog("Do you want to play a game?");

        // Yes or No?
        Conversation yesAnswer = new Conversation();
        yesAnswer.setId("601");
        yesAnswer.setDialog("Cool! Lets start...");

        Conversation noAnswer = new Conversation();
        noAnswer.setId("802");
        noAnswer.setDialog("Too bad!");

        _conversations.put(start.getId(), start);
        _conversations.put(yesAnswer.getId(), yesAnswer);
        _conversations.put(noAnswer.getId(), noAnswer);

        _graph = new ConversationGraph(_conversations, start.getId());

        ConversationChoice yesChoice = new ConversationChoice();
        yesChoice.setSourceId(start.getId());
        yesChoice.setDestinationId(yesAnswer.getId());
        yesChoice.setChoicePhrase("YES");

        ConversationChoice noChoice = new ConversationChoice();
        noChoice.setSourceId(start.getId());
        noChoice.setDestinationId(noAnswer.getId());
        noChoice.setChoicePhrase("NO");

        ConversationChoice startChoice01 = new ConversationChoice();
        startChoice01.setSourceId(yesAnswer.getId());
        startChoice01.setDestinationId(start.getId());
        startChoice01.setChoicePhrase("You suck! Game Over!");

        ConversationChoice startChoice02 = new ConversationChoice();
        startChoice02.setSourceId(noAnswer.getId());
        startChoice02.setDestinationId(start.getId());
        startChoice02.setChoicePhrase("Whats wrong with you...");

        _graph.addChoice(yesChoice);
        _graph.addChoice(noChoice);
        _graph.addChoice(startChoice01);
        _graph.addChoice(startChoice02);

        //System.out.println(_graph.toString());
        // System.out.println(_graph.displayCurrentConversation());
        //System.out.println(_graph.toJson());

    }

    public static Conversation getNextChoice(){
        ArrayList<ConversationChoice> choices = _graph.getCurrentChoices();
        for(ConversationChoice choice: choices){
            System.out.println(choice.getDestinationId() + " " + choice.getChoicePhrase());
        }

        ConversationChoice cc = choices.get(MathUtils.random(choices.size() - 1));
        System.out.println(cc.getChoicePhrase());
        _input = cc.getDestinationId();

        Conversation choice = null;
        try {
            choice = _graph.getConversationByID(_input);
        }catch( NumberFormatException nfe){
            return null;
        }
        return choice;
    }

    @Test
    public void testSimpleDialog() throws Exception {
        int cnt= 0;
        while( !_input.equalsIgnoreCase(quit) ){
            Conversation conversation = getNextChoice();
            if( conversation == null ) continue;
            _graph.setCurrentConversation(conversation.getId());
            System.out.println(_graph.displayCurrentConversation());
            cnt++;
            if (cnt > 6) {
                _input = quit;
            }
        }
    }
}
