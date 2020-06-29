package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Nathan Choi
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        /** FIXME */
        super(name, perm);
        _notches = notches;
    }

    /** FIXME? */
    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        /** FIXME */
        set(permutation().wrap(setting() + 1));
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (setting() == permutation().alphabet().
                    toInt(_notches.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /** FIXME: ADDITIONAL FIELDS HERE, AS NEEDED. */
    private String _notches;

}
