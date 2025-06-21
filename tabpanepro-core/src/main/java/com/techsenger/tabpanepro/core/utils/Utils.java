/*
 * Copyright (c) 2025 Pavel Castornii. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. This particular file is
 * subject to the "Classpath" exception as provided in the LICENSE file
 * that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.techsenger.tabpanepro.core.utils;

/**
 *
 * @author Pavel Castornii
 */
public final class Utils {

    private static final boolean mac = checkIfMac();

    private static boolean checkIfMac() {
        var osStr = System.getProperty("os.name");
        if (osStr == null) {
            return false;
        }
        return osStr.toLowerCase().startsWith("mac");
    }

    public static boolean isMac() {
        return mac;
    }

    public static double computeBoundedSize(double min, double pref, double max) {
        final double actualMax;
        if (min > max) {
            actualMax = min;
        } else {
            actualMax = max;
        }

        final double sizeWithMinGuarantee;
        if (pref < min) {
            sizeWithMinGuarantee = min;
        } else {
            sizeWithMinGuarantee = pref;
        }

        final double finalSize;
        if (sizeWithMinGuarantee > actualMax) {
            finalSize = actualMax;
        } else {
            finalSize = sizeWithMinGuarantee;
        }

        return finalSize;
    }

    private Utils() {
        //empty
    }
}
