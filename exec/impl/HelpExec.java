package sh4ll.exec.impl;

import sh4ll.Shell;
import sh4ll.etc.StateResult;
import sh4ll.exec.Exec;
import sh4ll.ui.textblock.def.NormalTextBlock;

public class HelpExec extends Exec {

    public HelpExec() {
        super(new String[]{"help"}, new String[]{"execname"}, "Gives info about execs");
    }


    @Override
    public byte runExec(String fullText, String[] splitted) {

        String execName = getInput("execname");
        if (execName != null) {
            final Exec exec = getExec(execName);
            if (exec == null) {
                Shell._self.writeLine(new NormalTextBlock("\247c exec not found"));
                return StateResult.EXIT_ON_ERROR;
            }
            Shell._self.writeLine(new NormalTextBlock("\2479" + exec.usage()));
            return StateResult.EXIT_ON_SUCCESS;
        }

        int longest = getLongestExecLength();
        for (Exec exec : Shell._self.getRegisteredExecs()) {
            if (exec.getTriggers().length > 0) {
                String space = putSpace(longest - exec.getTriggers()[0].length());
                    Shell._self.writeLine(new NormalTextBlock(exec.getTriggers()[0].toUpperCase() + "    " + exec.usage()));
            }
        }


        return 0;
    }

    public String putSpace(int times) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < times; i++) {
            space.append(" ");
        }
        return space.toString();
    }

    public int getLongestExecLength() {
        int LONGEST = 0;
        for (Exec exec : Shell._self.getRegisteredExecs()) {
            if (exec.getTriggers().length > 0) {
                if (LONGEST < exec.getTriggers()[0].length()) {
                    LONGEST = exec.getTriggers()[0].length();
                }
            }
        }
        return LONGEST;
    }

    @Override
    public String usage() {
        return "help [<execname>]";
    }
}
