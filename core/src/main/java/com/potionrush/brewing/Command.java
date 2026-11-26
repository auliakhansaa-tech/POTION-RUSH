package com.potionrush.brewing;
public interface Command {
    void execute();
    boolean undo();
}
