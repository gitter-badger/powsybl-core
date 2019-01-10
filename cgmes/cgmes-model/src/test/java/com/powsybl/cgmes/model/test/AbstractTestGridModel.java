/**
 * Copyright (c) 2017-2018, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.powsybl.cgmes.model.test;

import com.powsybl.cgmes.model.CgmesModel;

/**
 * @author Luma Zamarreño <zamarrenolm at aia.es>
 */
public abstract class AbstractTestGridModel implements TestGridModel {

    public AbstractTestGridModel(
            String name,
            CgmesModel expected) {
        this.name = name;
        this.expected = expected;
    }

    public CgmesModel expected() {
        return expected;
    }

    public String name() {
        return name;
    }

    private final String name;
    private final CgmesModel expected;
}
