/*
 * Copyright (c) 2026 Pedro Pathing
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Modifications by IamCoder18:
 *   - Added Synapse FtcOrchestrator lifecycle and a SafeHardwareMap field
 *     to the LinearOpMode body so Safe Pedro Pathing tuning procedures
 *     can be driven from this base class on a dedicated hardware thread.
 */
package org.firstinspires.ftc.teamcode.pedro.tuning.autotune;

import com.aaravlabs.synapse.ftc.FtcOrchestrator;
import com.aaravlabs.synapse.ftc.SafeHardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.concurrent.CountDownLatch;

public abstract class TuningOpMode<Result> extends LinearOpMode {
    public final String name;
    public final String description;
    public final boolean canStop;
    private final CountDownLatch finished = new CountDownLatch(1);
    private Throwable failure;
    private Result result;

    /**
     * Synapse safe hardware map backed by a dedicated hardware thread.
     * All Safe Pedro hardware access in tuners must go through this.
     */
    protected SafeHardwareMap safeMap;
    private com.aaravlabs.synapse.Orchestrator orchestrator;

    public TuningOpMode(String name, String description, boolean canStop) {
        this.name = name;
        this.description = description;
        this.canStop = canStop;
    }

    @Override
    public final void runOpMode() throws InterruptedException {
        orchestrator = FtcOrchestrator.create();
        safeMap = new SafeHardwareMap(hardwareMap, orchestrator.hardware());
        try {
            result = runTuningOpMode();
        } catch (InterruptedException | RuntimeException | Error exception) {
            failure = exception;
            throw exception;
        } finally {
            finished.countDown();
            orchestrator.close();
        }
    }

    protected abstract Result runTuningOpMode() throws InterruptedException;

    final Result awaitResult() throws InterruptedException {
        finished.await();
        if (failure instanceof InterruptedException) throw (InterruptedException) failure;
        if (failure instanceof RuntimeException) throw (RuntimeException) failure;
        if (failure instanceof Error) throw (Error) failure;
        return result;
    }
}
