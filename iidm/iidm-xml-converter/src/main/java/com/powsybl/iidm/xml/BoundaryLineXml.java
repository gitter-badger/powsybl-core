/**
 * Copyright (c) 2016, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.powsybl.iidm.xml;

import com.powsybl.commons.xml.XmlUtil;
import com.powsybl.iidm.network.BoundaryLine;
import com.powsybl.iidm.network.BoundaryLineAdder;
import com.powsybl.iidm.network.VoltageLevel;

import javax.xml.stream.XMLStreamException;

/**
 *
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
class BoundaryLineXml extends AbstractConnectableXml<BoundaryLine, BoundaryLineAdder, VoltageLevel> {

    static final BoundaryLineXml INSTANCE = new BoundaryLineXml();

    static final String ROOT_ELEMENT_NAME = "boundaryLine";

    static final String ROOT_ELEMENT_NAME_V10 = "danglingLine";

    private String currentRootElementName = ROOT_ELEMENT_NAME;

    @Override
    protected String getRootElementName() {
        return currentRootElementName;
    }

    @Override
    protected boolean hasSubElements(BoundaryLine dl) {
        return dl.getCurrentLimits() != null;
    }

    @Override
    protected void writeRootElementAttributes(BoundaryLine dl, VoltageLevel vl, NetworkXmlWriterContext context) throws XMLStreamException {
        XmlUtil.writeDouble("p0", dl.getP0(), context.getWriter());
        XmlUtil.writeDouble("q0", dl.getQ0(), context.getWriter());
        XmlUtil.writeDouble("r", dl.getR(), context.getWriter());
        XmlUtil.writeDouble("x", dl.getX(), context.getWriter());
        XmlUtil.writeDouble("g", dl.getG(), context.getWriter());
        XmlUtil.writeDouble("b", dl.getB(), context.getWriter());
        if (dl.getUcteXnodeCode() != null) {
            context.getWriter().writeAttribute("ucteXnodeCode", dl.getUcteXnodeCode());
        }
        writeNodeOrBus(null, dl.getTerminal(), context);
        writePQ(null, dl.getTerminal(), context.getWriter());
    }

    @Override
    protected void writeSubElements(BoundaryLine dl, VoltageLevel vl, NetworkXmlWriterContext context) throws XMLStreamException {
        if (dl.getCurrentLimits() != null) {
            writeCurrentLimits(null, dl.getCurrentLimits(), context.getWriter());
        }
    }

    @Override
    protected BoundaryLineAdder createAdder(VoltageLevel vl) {
        return vl.newBoundaryLine();
    }

    @Override
    protected BoundaryLine readRootElementAttributes(BoundaryLineAdder adder, NetworkXmlReaderContext context) {
        double p0 = XmlUtil.readDoubleAttribute(context.getReader(), "p0");
        double q0 = XmlUtil.readDoubleAttribute(context.getReader(), "q0");
        double r = XmlUtil.readDoubleAttribute(context.getReader(), "r");
        double x = XmlUtil.readDoubleAttribute(context.getReader(), "x");
        double g = XmlUtil.readDoubleAttribute(context.getReader(), "g");
        double b = XmlUtil.readDoubleAttribute(context.getReader(), "b");
        String ucteXnodeCode = context.getReader().getAttributeValue(null, "ucteXnodeCode");
        readNodeOrBus(adder, context);
        BoundaryLine dl = adder.setP0(p0)
                .setQ0(q0)
                .setR(r)
                .setX(x)
                .setG(g)
                .setB(b)
                .setUcteXnodeCode(ucteXnodeCode)
                .add();
        readPQ(null, dl.getTerminal(), context.getReader());
        return dl;
    }

    @Override
    protected void readSubElements(BoundaryLine dl, NetworkXmlReaderContext context) throws XMLStreamException {
        if (context.getVersion().equals("1_0")) {
            currentRootElementName = ROOT_ELEMENT_NAME_V10;
        }
        readUntilEndRootElement(context.getReader(), () -> {
            if ("currentLimits".equals(context.getReader().getLocalName())) {
                readCurrentLimits(null, dl::newCurrentLimits, context.getReader());
            } else {
                super.readSubElements(dl, context);
            }
        });
        if (context.getVersion().equals("1_0")) {
            currentRootElementName = ROOT_ELEMENT_NAME;
        }
    }
}