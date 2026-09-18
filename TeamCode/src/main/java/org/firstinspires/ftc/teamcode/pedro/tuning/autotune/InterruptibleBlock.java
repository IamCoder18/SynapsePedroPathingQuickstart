package org.firstinspires.ftc.teamcode.pedro.tuning.autotune;

@FunctionalInterface
public interface InterruptibleBlock {
    void execute() throws InterruptedException;
}
