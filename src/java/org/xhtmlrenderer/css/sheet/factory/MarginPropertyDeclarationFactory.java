/*
 * {{{ header & license
 * MarginPropertyDeclarationFactory.java
 * Copyright (c) 2004, 2005 Patrick Wright
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * }}}
 */
package org.xhtmlrenderer.css.sheet.factory;

import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;
import org.w3c.dom.DOMException;
import org.w3c.css.sac.LexicalUnit;
import org.xhtmlrenderer.css.constants.CSSName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A PropertyDeclarationFactory for CSS 2 "margin" shorthand property,
 * instantiating PropertyDeclarations; Singleton, use {@link #instance()}.
 *
 * @author   Patrick Wright
 */
public class MarginPropertyDeclarationFactory extends AbstractPropertyDeclarationFactory {
    private static final ZeroMarginPrimitiveValue ZERO_MARGIN = new ZeroMarginPrimitiveValue();

    /** Singleton instance. */
    private static MarginPropertyDeclarationFactory _instance;

    /**
     * List of property names, in order, when expanding 1 prop to 4. Careful,
     * this is closely in sync with the explosion code below, don't change
     * willy-nilly.
     */
    private final static CSSName ONE_TO_FOUR[] = {
            CSSName.MARGIN_TOP,
            CSSName.MARGIN_RIGHT,
            CSSName.MARGIN_BOTTOM,
            CSSName.MARGIN_LEFT};

    /**
     * List of property names, in order, when expanding 2 prop to 4. Careful,
     * this is closely in sync with the explosion code below, don't change
     * willy-nilly.
     */
    private final static CSSName TWO_TO_FOUR[] = {
            CSSName.MARGIN_TOP,
            CSSName.MARGIN_BOTTOM,
            CSSName.MARGIN_RIGHT,
            CSSName.MARGIN_LEFT};

    /**
     * List of property names, in order, when expanding 3 prop to 4. Careful,
     * this is closely in sync with the explosion code below, don't change
     * willy-nilly.
     */
    private final static CSSName THREE_TO_FOUR[] = {
            CSSName.MARGIN_TOP,
            CSSName.MARGIN_RIGHT,
            CSSName.MARGIN_LEFT,
            CSSName.MARGIN_BOTTOM};

    /**
     * List of property names, in order, when expanding 4 prop to 4. Careful,
     * this is closely in sync with the explosion code below, don't change
     * willy-nilly.
     */
    private final static CSSName FOUR_TO_FOUR[] = {
            CSSName.MARGIN_TOP,
            CSSName.MARGIN_RIGHT,
            CSSName.MARGIN_BOTTOM,
            CSSName.MARGIN_LEFT};

    /** Default constructor; don't use, use instance() instead. */
    private MarginPropertyDeclarationFactory() { }

    /**
     * Subclassed implementation of redirected buildDeclarations() from abstract
     * superclass.
     *
     * @param primVals   The SAC value for this property
     * @param important  True if author-marked important!
     * @param cssName    property name
     * @param origin     The origin of the stylesheet; constant from {@link
     *      org.xhtmlrenderer.css.sheet.Stylesheet}, e.g. Stylesheet.AUTHOR
     * @return           Iterator of PropertyDeclarations for the shorthand
     *      margin property.
     */
    protected Iterator doBuildDeclarations( CSSPrimitiveValue[] primVals,
                                            boolean important,
                                            CSSName cssName,
                                            int origin ) {

        List declarations = new ArrayList();

        // margin explodes differently based on number of supplied values
        CSSPrimitiveValue primitive = null;
        CSSPrimitiveValue primitives[] = null;

        switch ( primVals.length ) {
            case 1:
                primitive = primVals[0];
                primitives = new CSSPrimitiveValue[]{
                        hackAutoMargin(primitive),
                        hackAutoMargin(primitive),
                        hackAutoMargin(primitive),
                        hackAutoMargin(primitive)};

                addProperties( declarations, primitives, ONE_TO_FOUR, origin, important );
                break;
            case 2:
                primitives = new CSSPrimitiveValue[]{
                        hackAutoMargin(primVals[0]),
                        hackAutoMargin(primVals[0]),
                        hackAutoMargin(primVals[1]),
                        hackAutoMargin(primVals[1])};

                addProperties( declarations, primitives, TWO_TO_FOUR, origin, important );
                break;
            case 3:
                primitives = new CSSPrimitiveValue[]{
                        hackAutoMargin(primVals[0]),
                        hackAutoMargin(primVals[1]),
                        hackAutoMargin(primVals[1]),
                        hackAutoMargin(primVals[2])};

                addProperties( declarations, primitives, THREE_TO_FOUR, origin, important );
                break;
            case 4:
                primitives = new CSSPrimitiveValue[]{
                        hackAutoMargin(primVals[0]),
                        hackAutoMargin(primVals[1]),
                        hackAutoMargin(primVals[2]),
                        hackAutoMargin(primVals[3])};

                addProperties( declarations, primitives, FOUR_TO_FOUR, origin, important );
                break;
        }

        return declarations.iterator();
    }

    /**
     * Returns the singleton instance.
     *
     * @return   See desc.
     */
    public static synchronized PropertyDeclarationFactory instance() {
        if ( _instance == null ) {
            _instance = new MarginPropertyDeclarationFactory();
        }
        return _instance;
    }

    /** If CSSPrimitiveValue is 'auto', replace with "0", as we don't support auto-margins yet */
    // TODO: remove once auto margins are supported
    public static CSSPrimitiveValue hackAutoMargin(CSSPrimitiveValue margin) {
        if (margin.getCssText().trim().toLowerCase().equals("auto")) {
            return ZERO_MARGIN;
        } else {
            return margin;
        }
    }

    static class ZeroMarginPrimitiveValue implements CSSPrimitiveValue {

        public short getPrimitiveType() {
            return LexicalUnit.SAC_INTEGER;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public float getFloatValue(short unitType) throws DOMException {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setFloatValue(short unitType, float floatValue) throws DOMException {
            //noimpl
        }

        public String getStringValue() throws DOMException {
            return "0";  //noimpl
        }

        public void setStringValue(short stringType, String stringValue) throws DOMException {
            //noimpl
        }

        public Counter getCounterValue() throws DOMException {
            return null;  // noimpl
        }

        public RGBColor getRGBColorValue() throws DOMException {
            return null;  // noimpl
        }

        public Rect getRectValue() throws DOMException {
            return null;  // noimpl
        }

        public short getCssValueType() {
            return CSS_PRIMITIVE_VALUE;
        }

        public String getCssText() {
            return "0";
        }

        public void setCssText(String cssText) throws DOMException {
            // noimpl
        }
    }
}// end class

/*
 * $Id$
 *
 * $Log$
 * Revision 1.5  2006/07/26 18:05:34  pdoubleya
 * Hack to support margin: auto.
 *
 * Revision 1.4  2005/01/29 20:19:26  pdoubleya
 * Clean/reformat code. Removed commented blocks, checked copyright.
 *
 * Revision 1.3  2005/01/29 12:14:21  pdoubleya
 * Removed priority as a parameter, added alternate build when only CSSValue is available; could be used in a SAC DocumentHandler after the CSSValue is initialized from a property.
 *
 * Revision 1.2  2005/01/24 19:00:59  pdoubleya
 * Mass checkin. Changed to use references to CSSName, which now has a Singleton instance for each property, everywhere property names were being used before. Removed commented code. Cascaded and Calculated style now store properties in arrays rather than maps, for optimization.
 *
 * Revision 1.1  2005/01/24 14:25:36  pdoubleya
 * Added to CVS.
 *
 *
 */

