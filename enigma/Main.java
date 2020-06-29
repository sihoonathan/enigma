package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Nathan Choi
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine readyMachine = readConfig(); String currSettings = "";
        int numRotors = readyMachine.numRotors(); String curPlugBoardCycle = "";
        String[] currRotorToInsert = new String[numRotors];
        while (_input.hasNextLine()) {
            Scanner currStream = new Scanner(_input.nextLine());
            String msgToCode = "";
            if (currStream.hasNext("\\*")) {
                readyMachine.rotorReset(); currStream.next();
                ArrayList<String> currStringToProcess = new ArrayList<>();
                while (currStream.hasNext("[a-zA-Z\\d]+|\\([a-zA-Z\\d]+\\)")) {
                    currStringToProcess.add(currStream.next());
                }
                for (int i = 0; i < numRotors; i++) {
                    currRotorToInsert[i] = currStringToProcess.get(i); }
                ArrayList<String> rotorChecker = new ArrayList<>();
                for (int i = 0; i < currRotorToInsert.length; i++) {
                    if (rotorChecker.contains(currRotorToInsert[i])) {
                        throw new EnigmaException("Duplicate rotor name");
                    }
                    rotorChecker.add(currRotorToInsert[i]);
                    boolean rotorFound = false;
                    for (Rotor each : readyMachine.rotorReturn()) {
                        if (each.name().toUpperCase().equals
                                (currRotorToInsert[i].toUpperCase())) {
                            if (i == 0 && !each.reflecting()) {
                                throw new EnigmaException("Reflector"
                                        + " in wrong place");
                            }
                            rotorFound = true;
                        }
                    }
                    if (!rotorFound) {
                        throw new EnigmaException("Bad rotor name");
                    }
                }
                readyMachine.insertRotors(currRotorToInsert);
                currSettings = currStringToProcess.get(numRotors);
                setUp(readyMachine, currSettings);
                for (int k = numRotors + 1; k < currStringToProcess.size();
                     k++) {
                    if (currStringToProcess.get(k).matches("\\([a-zA-Z]+\\)")) {
                        curPlugBoardCycle += currStringToProcess.get(k) + " ";
                    } else {
                        break;
                    }
                }
                if (!curPlugBoardCycle.equals("")) {
                    readyMachine.setPlugboard(
                            new Permutation(curPlugBoardCycle, _alphabet));
                }
            } else {
                while (currStream.hasNext()) {
                    msgToCode += currStream.next();
                }
                String translated = readyMachine.convert(msgToCode);
                printMessageLine(translated);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        /** FIXME */
        try {
            String givenAlphabet = _config.next();
            _alphabet = new Alphabet(givenAlphabet);

            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            possibleRotorList = new ArrayList<>();
            while (_config.hasNext()) {
                possibleRotorList.add(readRotor());
            }

            return new Machine(_alphabet, numRotors, numPawls,
                    possibleRotorList);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        /** FIXME */
        try {
            String rotorName = _config.next();
            String movingNotches = _config.next();
            String permutation = "";
            Rotor readyRotor;

            while (_config.hasNext("\\(.+\\)")) {
                String tempPermutation = _config.next();
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

            if (movingNotches.charAt(0) == 'M' || movingNotches.charAt(0)
                    == 'm') {
                readyRotor = new MovingRotor(rotorName,
                        new Permutation(permutation, _alphabet),
                        movingNotches.substring(1));
            } else if (movingNotches.charAt(0) == 'N'
                    || movingNotches.charAt(0) == 'n') {
                readyRotor = new FixedRotor(rotorName,
                        new Permutation(permutation, _alphabet));
            } else {
                readyRotor = new Reflector(rotorName,
                        new Permutation(permutation, _alphabet));
            }

            return readyRotor;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        /** FIXME */
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        /** FIXME */
        String spacedString = "";
        for (int i = 0; i < msg.length(); i++) {
            if (i % 5 == 0 && i != 0) {
                spacedString += " " + msg.charAt(i);
            } else {
                spacedString += msg.charAt(i);
            }
        }
        _output.println(spacedString);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Additional. */
    private ArrayList<Rotor> possibleRotorList;
}
