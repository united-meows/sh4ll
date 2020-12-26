package sh4ll.exec.impl;

import sh4ll.Shell;
import sh4ll.etc.StateResult;
import sh4ll.exec.Exec;

public class ClearExec extends Exec {


    public ClearExec() {
        super(new String[]{"clear", "cls"}, new String[]{}, "Clears shell");
    }

    @Override
    public byte runExec(String fullText, String[] splitted) {
        Shell._self.outputs().clear();
        return StateResult.EXIT_ON_SUCCESS;
    }

    @Override
    public String usage() {
        return "clear|cls";
    }
}
