import java.util.ArrayList;
import java.util.Objects;

// categorize the squares and send it to their functions
// for add a new object change the marked parts (---------->)
public abstract class Categorize {
    public static void categorize(int y, int x) {
        if (Objects.equals(Main.Gamemap[y][x], "D")) {
            Diamond.dia(y, x);
        } else if (Objects.equals(Main.Gamemap[y][x], "S")) {
            Squar.squ(y, x);
        } else if (Objects.equals(Main.Gamemap[y][x], "T")) {
            Triangle.tri(y, x);
            Squar.squ(y, x);
        } else if (Objects.equals(Main.Gamemap[y][x], "W")) {
            Wildcard.wild(y, x);
        } else if (Objects.equals(Main.Gamemap[y][x], "+")) {
            Plus.plus(y, x);
        } else if (Objects.equals(Main.Gamemap[y][x], "-")) {
            Minus.minus(y, x);
        } else if (Objects.equals(Main.Gamemap[y][x], "/")) {
            Slash.slash(y, x);
        } else if (Objects.equals(Main.Gamemap[y][x], "\\")) {
            BackSlash.backslash(y, x);
        } else if (Objects.equals(Main.Gamemap[y][x], "|")) {
            VerticalBar.verticalbar(y, x);
        }  /*else if (Objects.equals(Main.Gamemap[y][x], "yourmark")) {                 -------------------->
            Yournewclass.yourmethod(y, x);*/
        else{
            Main.isValid = false;
            return;
        }
        Main.isValid = true;
    }

    //hold the scores
    public static void lettertopint(ArrayList<String> stringArrayList) {
        Main.currentscore = 0;
        for (String s : stringArrayList) {
            Main.currentscore += Objects.equals(s, "D") ? 30 : 0;
            Main.currentscore += Objects.equals(s, "W") ? 10 : 0;
            Main.currentscore += Objects.equals(s, "S") ? 15 : 0;
            Main.currentscore += Objects.equals(s, "T") ? 15 : 0;
            Main.currentscore += Objects.equals(s, "/") ? 20 : 0;
            Main.currentscore += Objects.equals(s, "-") ? 20 : 0;
            Main.currentscore += Objects.equals(s, "+") ? 20 : 0;
            Main.currentscore += Objects.equals(s, "\\") ? 20 : 0;
            Main.currentscore += Objects.equals(s, "|") ? 20 : 0;
            //Main.currentscore += Objects.equals(s, "yourmark") ? yourmarkspoint : 0;    -------------------->
        }
        Main.globalscore += Main.currentscore;
    }
}


// in these classes hold the direction data and send to delete methods

/*abstract class YourNewClass {            ------------------------------------->

    public static void yourmethod(int y, int x) {
        if (delet.delete(y, x, -1, -1, false)) {   ------> it holds the direction data and math data -1 -1 for top left
        } else if (delet.delete(y, x, +1, +1, false)) { --------> +1 +1 for bottom right
        }
    }
}
*/
abstract class Diamond {
    public static void dia(int y, int x) {
        if (delet.delete(y, x, -1, -1, false)) {
        } else if (delet.delete(y, x, +1, +1, false)) {
        } else if (delet.delete(y, x, -1, +1, false)) {
        } else if (delet.delete(y, x, +1, -1, false)) {
        }
    }
}


abstract class Squar {
    public static void squ(int y, int x) {
        if (delet.delete(y, x, 0, -1, false)) {
        } else if (delet.delete(y, x, 0, +1, false)) {
        }
    }
}

abstract class Triangle {
    public static void tri(int y, int x) {
        if (delet.delete(y, x, -1, 0, false)) {
        } else if (delet.delete(y, x, +1, 0, false)) {
        }
    }
}

abstract class Wildcard {
    public static void wild(int y, int x) {
        if (delet.delete(y, x, -1, 0, false)) {
        } else if (delet.delete(y, x, +1, 0, false)) {
        } else if (delet.delete(y, x, 0, -1, false)) {
        } else if (delet.delete(y, x, 0, +1, false)) {
        } else if (delet.delete(y, x, -1, -1, false)) {
        } else if (delet.delete(y, x, +1, +1, false)) {
        } else if (delet.delete(y, x, -1, +1, false)) {
        } else if (delet.delete(y, x, +1, -1, false)) {
        }
    }
}

abstract class Slash {
    public static void slash(int y, int x) {
        if (delet.delete(y, x, -1, +1, true)) {
        } else if (delet.delete(y, x, +1, -1, true)) {
        }
    }
}

abstract class Minus {
    public static void minus(int y, int x) {
        if (delet.delete(y, x, 0, -1, true)) {
        } else if (delet.delete(y, x, 0, +1, true)) {
        }
    }
}

abstract class BackSlash {
    public static void backslash(int y, int x) {
        if (delet.delete(y, x, -1, -1, true)) {
        } else if (delet.delete(y, x, +1, +1, true)) {
        }
    }
}

abstract class VerticalBar {
    public static void verticalbar(int y, int x) {
        if (delet.delete(y, x, -1, 0, true)) {
        } else if (delet.delete(y, x, +1, 0, true)) {
        }
    }
}

abstract class Plus {
    public static void plus(int y, int x) {
        if (delet.delete(y, x, 0, -1, true)) {
        } else if (delet.delete(y, x, 0, +1, true)) {
        } else if (delet.delete(y, x, -1, 0, true)) {
        } else if (delet.delete(y, x, +1, 0, true)) {
        }
    }
}

