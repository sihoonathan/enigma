package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Nathan Choi
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        /** FIXME */
        _alphabet = alphabet;

        String cyclesTemp = cycles.trim();
        cyclesTemp = cyclesTemp.replace("(", "");
        cyclesTemp = cyclesTemp.replace(")", "");
        _cycles = cyclesTemp.split("\\s+");

    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        /** FIXME */
        String[] addedCycle = new String[_cycles.length + 1];
        System.arraycopy(_cycles, 0, addedCycle, 0, _cycles.length);
        addedCycle[addedCycle.length - 1] = cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        /** FIXME */
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        /** FIXME */
        char pIn = alphabet().toChar(wrap(p));
        char pOut;

        for (String cycle : _cycles) {
            for (int i = 0; i < cycle.length(); i++) {
                if (pIn == cycle.charAt(i)) {
                    pOut = cycle.charAt(Math.floorMod((i + 1), cycle.length()));
                    return _alphabet.toInt(pOut);
                }
            }
        }

        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        /** FIXME */
        char cIn = alphabet().toChar(wrap(c));
        char cOut;

        for (String cycle : _cycles) {
            for (int i = 0; i < cycle.length(); i++) {
                if (cIn == cycle.charAt(i)) {
                    cOut = cycle.charAt(Math.floorMod((i - 1), cycle.length()));
                    return _alphabet.toInt(cOut);
                }
            }
        }

        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        /** FIXME */
        int pIn = _alphabet.toInt(p);
        char pOut = _alphabet.toChar(permute(pIn));

        return pOut;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        /** FIXME */
        int cIn = _alphabet.toInt(c);
        char cOut = _alphabet.toChar(invert(cIn));

        return cOut;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        /** FIXME */
        int total = 0;

        for (String cycle : _cycles) {
            total += cycle.length();
        }

        return total == size();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /**
     * FIXME: ADDITIONAL FIELDS HERE, AS NEEDED.
     */

    private String[] _cycles;

}
