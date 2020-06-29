package enigma;

import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void readRotorTest() {
        String permutationRotor1 = "";
        String permutationRotor5 = "";
        String reflectorC = "";
        try {
            File text = new File("/Users/nathan/Documents/Courses"
                    + "/Fa19/61B/repo/proj1/testing/correct/default.conf");
            Scanner config = new Scanner(text);
            config.nextLine();
            config.nextLine();
            int i = 0;
            while (i < 12) {
                String rotorName = config.next();
                String movingNotches = config.next();
                String permutation = "";
                Rotor readyRotor;
                while (config.hasNext("\\(.+\\)")) {
                    String tempPermutation = config.next();
                    String[] tempPermArray;
                    if (tempPermutation.contains(")(")) {
                        tempPermArray = tempPermutation.split("\\)\\(");
                        for (String each : tempPermArray) {
                            if (!each.contains(")")) {
                                permutation += " " + each + ")";
                            } else {
                                permutation += " (" + each;
                            }
                        }
                        continue;
                    }
                    permutation += " " + tempPermutation;
                }
                if (i == 0) {
                    permutationRotor1 = permutation;
                    assertEquals(" " + "(AELTPHQXRU) (BKNW) "
                            + "(CMOY) (DFG) (IV) (JZ) (S)", permutationRotor1);
                }

                if (i == 4) {
                    permutationRotor5 = permutation;
                    assertEquals(" "
                            + "(AVOLDRWFIUQ) (BZKSMNHYC) (EGTJPX)",
                            permutationRotor5);

                }

                if (i == 11) {
                    reflectorC = permutation;
                    assertEquals(" (AR) (BD) (CO) (EJ) (FN) "
                            + "(GT) (HK) (IV) (LM) (PW) (QZ) (SX) (UY)",
                            reflectorC);
                }
                i++;
                System.out.println(permutation);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

   /* public static void main(String[] args) {
        readRotorTest();
    }*/
}
