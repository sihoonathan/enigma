package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;


/** Class that represents a complete enigma machine.
 *  @author Nathan Choi
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        /** FIXME */
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotorOrder = new ArrayList<>();
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        /** FIXME */
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        /** FIXME */
        return _pawls;
    }

    /** additional. */
    void rotorReset() {
        _rotorOrder = new ArrayList<>();
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        /** FIXME */
        HashMap<String, Rotor> rotorMap = new HashMap<>();

        for (Rotor rotor : _allRotors) {
            rotorMap.put(rotor.name().toUpperCase(), rotor);
        }

        for (String rotorName : rotors) {
            _rotorOrder.add(rotorMap.get(rotorName.toUpperCase()));
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        /** FIXME */
        if (setting.length() != (_numRotors - 1)) {
            throw new EnigmaException("Setting and numRotors do not match");
        }

        char[] settingArray = setting.toCharArray();

        for (int i = 0; i < settingArray.length; i++) {
            _rotorOrder.get(i + 1).set(settingArray[i]);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        /** FIXME */
        _plugBoard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        /** FIXME */

        for (int i = 0; i < _rotorOrder.size(); i++) {
            int rightMostRotorIndex = _rotorOrder.size() - 1;
            Rotor currRotor = _rotorOrder.get(i);

            if (i == rightMostRotorIndex) {
                currRotor.advance();
                break;
            }

            Rotor nextRotor = _rotorOrder.get(i + 1);

            if (!currRotor.reflecting() && currRotor.rotates()
                    && nextRotor.atNotch()) {
                for (int k = i; k < _rotorOrder.size(); k++) {
                    _rotorOrder.get(k).advance();
                }
                break;
            }
        }
        if (_plugBoard != null) {
            c = _plugBoard.permute(c);
        }

        for (int i = _rotorOrder.size() - 1; i >= 0; i--) {
            Rotor currRotor = _rotorOrder.get(i);
            c = currRotor.convertForward(c);
        }

        for (int i = 1; i < _rotorOrder.size(); i++) {
            Rotor currRotor = _rotorOrder.get(i);
            c = currRotor.convertBackward(c);
        }

        if (_plugBoard != null) {
            c = _plugBoard.permute(c);
        }

        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        /** FIXME */
        String convertedMsg = "";
        char[] msgLetters = msg.toCharArray();

        for (char letter : msgLetters) {
            convertedMsg += _alphabet.toChar(convert(_alphabet.toInt(letter)));
        }

        return convertedMsg;
    }

    /** additional. @return*/
    public Collection<Rotor> rotorReturn() {
        return _allRotors;
    }


    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** FIXME: ADDITIONAL FIELDS HERE, IF NEEDED. */
    /** additional. */
    private final int _numRotors;

    /** additional. */
    private final int _pawls;

    /** additional. */
    private Collection<Rotor> _allRotors;

    /** additional. */
    private ArrayList<Rotor> _rotorOrder;

    /** additional. */
    private Permutation _plugBoard;
}
