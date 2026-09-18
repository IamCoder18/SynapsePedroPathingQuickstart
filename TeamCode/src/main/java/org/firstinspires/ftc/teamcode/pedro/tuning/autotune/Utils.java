package org.firstinspires.ftc.teamcode.pedro.tuning.autotune;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.*;

public final class Utils {
    public static String nanoid() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, 6);
    }
}
