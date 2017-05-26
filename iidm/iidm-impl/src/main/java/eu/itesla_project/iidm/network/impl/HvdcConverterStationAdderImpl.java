/**
 * Copyright (c) 2017, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package eu.itesla_project.iidm.network.impl;

/**
 * @author Mathieu Bague <mathieu.bague at rte-france.com>
 */
public abstract class HvdcConverterStationAdderImpl<T extends HvdcConverterStationAdderImpl<T>> extends SingleTerminalConnectableAdderImpl<T> {

    private final VoltageLevelExt voltageLevel;

    private float lossFactor = Float.NaN;

    HvdcConverterStationAdderImpl(VoltageLevelExt voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    @Override
    protected NetworkImpl getNetwork() {
        return voltageLevel.getNetwork();
    }

    protected VoltageLevelExt getVoltageLevel() {
        return voltageLevel;
    }

    public float getLossFactor() {
        return lossFactor;
    }

    public T setLossFactor(float lossFactor) {
        this.lossFactor = lossFactor;
        return (T) this;
    }

    protected void validate() {
        ValidationUtil.checkLossFactor(this, lossFactor);
    }
}