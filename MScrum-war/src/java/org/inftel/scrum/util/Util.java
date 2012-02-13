/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.util;

import java.security.MessageDigest;

/**
 *
 * @author Jorge
 */
public final class Util {
    /**
     * Devuelve en un string la codificación en md5 de la cadena de entrada.
     *
     * @param cadPass Cadena a codidifcar
     * @return Cadena con la coficación en md5 de la cadena de entrada.
     * @throws java.lang.Exception
     */
    public static String md5(String cadPass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(cadPass.getBytes());

            int size = b.length;
            StringBuilder h = new StringBuilder(size);
            for (int i = 0; i < size; i++) {
                int u = b[i] & 255;
                if (u < 16) {
                    h.append("0").append(Integer.toHexString(u));
                } else {
                    h.append(Integer.toHexString(u));
                }
            }
            return h.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
