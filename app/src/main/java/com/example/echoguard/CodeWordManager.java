package com.example.echoguard;

import android.content.Context;

public class CodeWordManager {
    private final Context context;

    public CodeWordManager(Context context) {
        this.context = context;
    }

    public String getCodeWord() {
        Database database = new Database(context);
        return database.getCodeWord(); // Ensure getCodeWord() is implemented in Database class
    }

    public void addCodeWord(String codeWord) {
        Database database = new Database(context);
        database.addCodeWord(codeWord); // Ensure addCodeWord() is implemented in Database class
    }
}
