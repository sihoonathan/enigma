package enigma;

import org.junit.Test;
import java.util.ArrayList;
import static enigma.TestUtils.UPPER;
import static org.junit.Assert.*;

public class MachineTest {
    static final ArrayList<Rotor> ALL_ROTORS = new ArrayList<>();
    static {
        ALL_ROTORS.add(new Reflector("B",
                new Permutation("AE) " + "(BN) "
                        + "(CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) "
                        + "(RX) (SZ) (TV)", UPPER)));
        ALL_ROTORS.add(new FixedRotor("BETA",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) "
                        + "(HIX)", UPPER)));
        ALL_ROTORS.add(new MovingRotor("III",
                new Permutation("(ABDHPEJT) "
                        + "(CFLVMZOYQIRWUKXSG) (N)", UPPER), "V"));
        ALL_ROTORS.add(new MovingRotor("IV",
                new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) "
                        + "(DV) (KU)", UPPER), "J"));
        ALL_ROTORS.add(new MovingRotor("I",
                new Permutation("(AELTPHQXRU) "
                        + "(BKNW) (CMOY) (DFG) (IV)"
                        + " (JZ) (S)", UPPER), "Q"));
    }

    @Test
    public void machineRun() {
        Machine testMachine = new Machine(UPPER, 5, 3, ALL_ROTORS);
        String[] machineNames = new String[]{"B", "BETA", "III", "IV", "I"};
        Permutation plugBoard = new Permutation("(YF) (ZH)", UPPER);
        testMachine.insertRotors(machineNames);
        testMachine.setPlugboard(plugBoard);

        testMachine.setRotors("AXLE");
        assertEquals(testMachine.convert(UPPER.toInt('Z')), UPPER.toInt('Y'));

        testMachine.setRotors("AXLE");
        assertEquals(testMachine.convert(UPPER.toInt('Y')), UPPER.toInt('Z'));
    }
}

