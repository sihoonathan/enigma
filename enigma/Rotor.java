package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Nathan Choi
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        /** FIXME */
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        /** FIXME */
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        /** FIXME */
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        /** FIXME */
        _setting = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        /** FIXME */
        int contactIn = _permutation.wrap(p + setting());
        int permutedIn = _permutation.permute(contactIn);
        int contactOut = _permutation.wrap(permutedIn - setting());
        return contactOut;

    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        /** FIXME */
        int contactIn = _permutation.wrap(e + setting());
        int permutedIn = _permutation.invert(contactIn);
        int contactOut = _permutation.wrap(permutedIn - setting());
        return contactOut;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** FIXME : ADDITIONAL FIELDS HERE, AS NEEDED. */
    private int _setting;
}
