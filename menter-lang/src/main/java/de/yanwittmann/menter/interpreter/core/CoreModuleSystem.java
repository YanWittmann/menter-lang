package de.yanwittmann.menter.interpreter.core;

import de.yanwittmann.menter.interpreter.structure.value.Value;

public class CoreModuleSystem {

    public static Value getProperty(Value[] arguments) {
        return new Value(System.getProperty(arguments[0].toString()));

    }

    public static Value getEnv(Value[] arguments) {
        return new Value(System.getenv(arguments[0].toString()));
    }

    public static Value sleep(Value[] arguments) {
        try {
            Thread.sleep(arguments[0].getNumericValue().longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return arguments[0];
    }
}
