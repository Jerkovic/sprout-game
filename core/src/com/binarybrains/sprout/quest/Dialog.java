package com.binarybrains.sprout.quest;

import java.util.List;
import java.util.Optional;

public class Dialog {

    private int id;
    private boolean hidden = false;
    private String npcText; // list paginated?
    private String playerText;
    private Optional<Quest> quest;
    private List<Requirement> requirements;
    private List<Dialog> responses;

    public Dialog(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getNpcText() {
        return npcText;
    }

    public Dialog setNpcText(String npcText) {
        this.npcText = npcText;
        return this;
    }

    public String getPlayerText() {
        return playerText;
    }

    public void setPlayerText(String playerText) {
        this.playerText = playerText;
    }

    public Optional<Quest> getQuest() {
        return quest;
    }

    public void setQuest(Optional<Quest> quest) {
        this.quest = quest;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public List<Dialog> getResponses() {
        return responses;
    }

    public void setResponses(List<Dialog> responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        return "DialogNode{" +
                "id=" + id + ", npcText=" + npcText +
                '}';
    }
}
